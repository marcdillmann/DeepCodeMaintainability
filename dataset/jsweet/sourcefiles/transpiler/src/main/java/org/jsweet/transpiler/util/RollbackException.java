/* 
 * JSweet transpiler - http://www.jsweet.org
 * Copyright (C) 2015 CINCHEO SAS <renaud.pawlak@cincheo.fr>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.jsweet.transpiler.util;

import java.util.function.Consumer;

import com.sun.tools.javac.tree.JCTree;

/**
 * This exception can be thrown to rollback the scanning of an AST.
 * 
 * @author Renaud Pawlak
 */
public class RollbackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private JCTree target;
	private Consumer<JCTree> onRollbacked;

	/**
	 * Rollback up to the target.
	 * 
	 * @param target
	 *            the target
	 * @param onRollbacked
	 *            the handler to be executed once rollbacked.
	 */
	public RollbackException(JCTree target, Consumer<JCTree> onRollbacked) {
		super();
		this.target = target;
		this.onRollbacked = onRollbacked;
	}

	/**
	 * Gets the target of the rollback.
	 */
	public JCTree getTarget() {
		return target;
	}

	/**
	 * Gets the rollback handler.
	 */
	public Consumer<JCTree> getOnRollbacked() {
		return onRollbacked;
	}

}
