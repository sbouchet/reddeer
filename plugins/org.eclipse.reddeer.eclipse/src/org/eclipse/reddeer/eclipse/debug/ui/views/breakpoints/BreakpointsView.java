/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat, Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.reddeer.eclipse.debug.ui.views.breakpoints;

import java.util.List;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.condition.WidgetIsFound;
import org.eclipse.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.exception.EclipseLayerException;
import org.eclipse.reddeer.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.eclipse.reddeer.jface.wizard.WizardDialog;
import org.eclipse.reddeer.swt.api.Button;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.api.ToolItem;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ControlIsEnabled;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.YesButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;

/**
 * Breakpoints view
 * 
 * @author Andrej Podhradsky
 * @author Tomas Sedmik
 *
 */
public class BreakpointsView extends WorkbenchView {

	/**
	 * Instantiates a new breakpoints view.
	 */
	public BreakpointsView() {
		super("Breakpoints");
	}

	/**
	 * Adds a given java exception breakpoint.
	 *
	 * @param exception            java exception
	 */
	public void addJavaExceptionBreakpoint(String exception) {
		log.info("Adding java exception breakpoint '" + exception + "'");
		open();
		new DefaultToolItem(cTabItem.getFolder(), "Add Java Exception Breakpoint").click();
		FilteredItemsSelectionDialog breakpointsDialog = new FilteredItemsSelectionDialog(new WithTextMatcher("Add Java Exception Breakpoint"));
		breakpointsDialog.setFilterText(exception);
		new WaitUntil(new ControlIsEnabled(new OkButton(breakpointsDialog)), TimePeriod.LONG);
		breakpointsDialog.ok();
	};
	
	/**
	 * Returns whether a breakpoint is available in Breakpoints View.
	 *
	 * @param label            label or some label's substring of the breakpoint
	 * @return true if a breakpoint is present, false otherwise
	 */
	public boolean isBreakpointAvailable(String label) {
		open();
		return getBreakpoint(label) == null ? false : true;
	}

	/**
	 * Gets a particular breakpoint in Breakpoints view.
	 *
	 * @param label            Label or some label's substring of the breakpoint
	 * @return breakpoint - there is some breakpoint with given label, null -
	 *         otherwise
	 */
	public Breakpoint getBreakpoint(String label) {
		log.info("Accessing breakpoints in Breakpoints view");
		open();
		AbstractWait.sleep(TimePeriod.SHORT);
		List<TreeItem> items = new DefaultTree(this).getItems();
		for (TreeItem item : items) {
			log.debug("\tfound: " + item.getText());
			if (item.getText().contains(label)) {
				return new Breakpoint(item);
			}
		}
		return null;
	}

	/**
	 * Removes all breakpoints.
	 */
	public void removeAllBreakpoints() {
		log.info("Removing all breakpoints from Breakpoints view");
		open();
		ToolItem removeBreakpointsItem = new DefaultToolItem(cTabItem.getFolder(), "Remove All Breakpoints");
		if (removeBreakpointsItem.isEnabled()) {
			removeBreakpointsItem.click();
			Shell removeBreakpointsShell = new DefaultShell("Remove All Breakpoints");
			
			//photon changed button text
			WidgetIsFound removeButtonTest = new WidgetIsFound(org.eclipse.swt.widgets.Button.class, removeBreakpointsShell.getControl(),
					new WithMnemonicTextMatcher("Remove"));
			Button button;
			if(removeButtonTest.test()){
				button = new PushButton((org.eclipse.swt.widgets.Button)removeButtonTest.getResult());
			} else {
				button = new YesButton(removeBreakpointsShell);	
			}
			button.click();
			new WaitWhile(new ShellIsAvailable(removeBreakpointsShell));
		}
	}

	/**
	 * Imports breakpoints from a file.
	 *
	 * @param path            path to the file
	 */
	public void importBreakpoints(String path) {
		log.info("Importing breakpoints from '" + path + "'");
		open();
		new DefaultTree(this).getContextMenu().getItem("Import Breakpoints...").select();
		WizardDialog importBreakpointsDialog = new WizardDialog("Import Breakpoints");
		importBreakpointsDialog.isOpen();
		new LabeledText(importBreakpointsDialog, "From file:").setText(path);
		new WaitUntil(new AbstractWaitCondition() {
			
			@Override
			public boolean test() {
				return importBreakpointsDialog.isFinishEnabled();
			}
		}, TimePeriod.SHORT, false);
		if(!importBreakpointsDialog.isFinishEnabled()) {
			throw new EclipseLayerException("Finish button is not enabled. Dialog error is '"
					+importBreakpointsDialog.getMessage()+"'");
		}
		importBreakpointsDialog.finish();
	}
}
