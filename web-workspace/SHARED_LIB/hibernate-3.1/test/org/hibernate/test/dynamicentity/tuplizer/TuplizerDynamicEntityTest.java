package org.hibernate.test.dynamicentity.tuplizer;

import org.hibernate.test.TestCase;
import org.hibernate.test.dynamicentity.Company;
import org.hibernate.test.dynamicentity.ProxyHelper;
import org.hibernate.test.dynamicentity.Customer;
import org.hibernate.test.dynamicentity.Address;
import org.hibernate.test.dynamicentity.Person;
import org.hibernate.Session;
import org.hibernate.Hibernate;
import junit.framework.TestSuite;

import java.util.HashSet;

/**
 * Demonstrates use of Tuplizers to allow the use of JDK
 * {@link java.lang.reflect.Proxy dynamic proxies} as our
 * domain model.
 * <p/>
 * Here we plug a custom Interceptor into the session simply to
 * allow us to not have to explicitly supply the appropriate entity
 * name to the Session calls.
 *
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class TuplizerDynamicEntityTest extends TestCase {
	public TuplizerDynamicEntityTest(String x) {
		super( x );
	}

	protected String[] getMappings() {
		return new String[] { "dynamicentity/tuplizer/Customer.hbm.xml" };
	}

	public static TestSuite suite() {
		return new TestSuite( TuplizerDynamicEntityTest.class );
	}

	public void testIt() {
		// Test saving these dyna-proxies
		Session session = openSession( new EntityNameInterceptor() );
		session.beginTransaction();
		Company company = ProxyHelper.newCompanyProxy();
		company.setName( "acme" );
		session.save( company );
		Customer customer = ProxyHelper.newCustomerProxy();
		customer.setName( "Steve" );
		customer.setCompany( company );
		Address address = ProxyHelper.newAddressProxy();
		address.setStreet( "somewhere over the rainbow" );
		address.setCity( "lawerence, kansas" );
		address.setPostalCode( "toto");
		customer.setAddress( address );
		customer.setFamily( new HashSet() );
		Person son = ProxyHelper.newPersonProxy();
		son.setName( "son" );
		customer.getFamily().add( son );
		Person wife = ProxyHelper.newPersonProxy();
		wife.setName( "wife" );
		customer.getFamily().add( wife );
		session.save( customer );
		session.getTransaction().commit();
		session.close();

		assertNotNull( "company id not assigned", company.getId() );
		assertNotNull( "customer id not assigned", customer.getId() );
		assertNotNull( "address id not assigned", address.getId() );
		assertNotNull( "son:Person id not assigned", son.getId() );
		assertNotNull( "wife:Person id not assigned", wife.getId() );

		// Test loading these dyna-proxies, along with flush processing
		session = openSession( new EntityNameInterceptor() );
		session.beginTransaction();
		customer = ( Customer ) session.load( Customer.class, customer.getId() );
		assertFalse( "should-be-proxy was initialized", Hibernate.isInitialized( customer ) );

		customer.setName( "other" );
		session.flush();
		assertFalse( "should-be-proxy was initialized", Hibernate.isInitialized( customer.getCompany() ) );

		session.refresh( customer );
		assertEquals( "name not updated", "other", customer.getName() );
		assertEquals( "company association not correct", "acme", customer.getCompany().getName() );

		session.getTransaction().commit();
		session.close();

		// Test detached entity re-attachment with these dyna-proxies
		customer.setName( "Steve" );
		session = openSession( new EntityNameInterceptor() );
		session.beginTransaction();
		session.update( customer );
		session.flush();
		session.refresh( customer );
		assertEquals( "name not updated", "Steve", customer.getName() );
		session.getTransaction().commit();
		session.close();

		// Test querying
		session = openSession( new EntityNameInterceptor() );
		session.beginTransaction();
		int count = session.createQuery( "from Customer" ).list().size();
		assertEquals( "querying dynamic entity", 1, count );
		session.clear();
		count = session.createQuery( "from Person" ).list().size();
		assertEquals( "querying dynamic entity", 3, count );
		session.getTransaction().commit();
		session.close();
	}
}
