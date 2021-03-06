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
package org.eclipse.reddeer.eclipse.epp.logging.aeri.ide.dialogs;

import java.util.List;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.swt.api.Combo;
import org.eclipse.reddeer.swt.api.Group;
import org.eclipse.reddeer.swt.api.Text;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Represends AERI preference page
 * 
 * @author rawagner
 *
 */
public class PreferencePage extends org.eclipse.reddeer.jface.preference.PreferencePage {

	public static final String CATEGORY = "General";
	public static final String PAGE_NAME = "Error Reporting";

	public PreferencePage(ReferencedComposite referencedComposite) {
		super(referencedComposite, CATEGORY, PAGE_NAME);
	}

	public PreferencePage setName(String name) {
		getNameWidget().setText(name);
		return this;
	}

	public PreferencePage setEmail(String email) {
		getEmailWidget().setText(email);
		return this;
	}

	public String getName() {
		return getNameWidget().getText();
	}

	public String getEmail() {
		return getEmailWidget().getText();
	}

	public PreferencePage anonymizePackageClassMethodNames(boolean toggle) {
		getAnonymizePackageWidget().toggle(toggle);
		return this;
	}

	public boolean isAnonymizePackageClassMethodNames() {
		return getAnonymizePackageWidget().isChecked();
	}

	public PreferencePage anonymizeErrorLogMessages(boolean toggle) {
		getAnonymizeErrorWidget().toggle(toggle);
		return this;
	}

	public boolean isAnonymizeErrorLogMessages() {
		return getAnonymizeErrorWidget().isChecked();
	}

	public PreferencePage disableExtendedClasspathAnalysis(boolean toggle) {
		getDisableAnalysisWidget().toggle(toggle);
		return this;
	}

	public boolean isDisableExtendedClasspathAnalysis() {
		return getDisableAnalysisWidget().isChecked();
	}

	public PreferencePage enableDebugMode(boolean toggle) {
		getDebugModeWidget().toggle(toggle);
		return this;
	}

	public boolean isEnableDebugMode() {
		return getDebugModeWidget().isChecked();
	}

	public PreferencePage setSendMode(String sendMode) {
		getSendModeWidget().setSelection(sendMode);
		return this;
	}

	public String getSendMode() {
		return getSendModeWidget().getSelection();
	}

	public List<String> getAvailableSendModes() {
		return getSendModeWidget().getItems();
	}

	public SetupWizard openConfigureProjects() {
		new PushButton(this, "Configure Projects…").click();
		return new SetupWizard();
	}

	protected Group getSendOptionsGroup() {
		return new DefaultGroup(this, "Send Options");
	}

	protected Group getContactGroup() {
		return new DefaultGroup(this, "Contact Information");
	}

	protected Text getNameWidget() {
		return new LabeledText(getContactGroup(), "Name:");
	}

	protected Text getEmailWidget() {
		return new LabeledText(getContactGroup(), "E‐mail:");
	}

	protected CheckBox getAnonymizePackageWidget() {
		return new CheckBox(getSendOptionsGroup(), "Anonymize package, class, and method names");
	}

	protected CheckBox getAnonymizeErrorWidget() {
		return new CheckBox(getSendOptionsGroup(), "Anonymize error log messages");
	}

	protected CheckBox getDisableAnalysisWidget() {
		return new CheckBox(getSendOptionsGroup(), "Disable extended classpath analysis");
	}

	protected CheckBox getDebugModeWidget() {
		return new CheckBox(this, "Enable debug mode");
	}

	protected Combo getSendModeWidget() {
		return new LabeledCombo(this, "Send mode:");
	}

}
