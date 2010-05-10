// $Id: HeresAnotherCrazyIdFieldName.java,v 1.1 2005/07/14 14:58:28 steveebersole Exp $
package org.hibernate.test.hql;

/**
 * Implementation of HeresAnotherCrazyIdFieldName.
 *
 * @author Steve Ebersole
 */
public class HeresAnotherCrazyIdFieldName {
	private Long heresAnotherCrazyIdFieldName;
	private String name;

	public HeresAnotherCrazyIdFieldName() {
	}

	public HeresAnotherCrazyIdFieldName(String name) {
		this.name = name;
	}

	public Long getHeresAnotherCrazyIdFieldName() {
		return heresAnotherCrazyIdFieldName;
	}

	public void setHeresAnotherCrazyIdFieldName(Long heresAnotherCrazyIdFieldName) {
		this.heresAnotherCrazyIdFieldName = heresAnotherCrazyIdFieldName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}