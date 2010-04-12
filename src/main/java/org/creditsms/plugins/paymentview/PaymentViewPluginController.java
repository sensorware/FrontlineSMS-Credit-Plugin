/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright (C) - 2009, 2010
 * 
 * This file is part of FrontlineSMS:Credit
 * 
 */
package org.creditsms.plugins.paymentview;

import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
import org.springframework.context.ApplicationContext;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.domain.Message;
import net.frontlinesms.listener.IncomingMessageListener;
import net.frontlinesms.plugins.BasePluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.ui.UiGeneratorController;

/**
 * This is the base class for the FrontlineSMS:Credit PaymentView plugin. The PaymentView
 * plugin is used to process payments transacted via the connected mobile phone. Processing
 * of the payments includes mining the incoming message for specific information and pushing
 * the same to an online/external database or system such as Mifos - http://www.mifos.org
 * 
 * 
 * @author Emmanuel Kala
 *
 */
@PluginControllerProperties(name="Payment View", iconPath="/icons/creditsms.png",
		springConfigLocation=PluginControllerProperties.NO_VALUE,
		hibernateConfigPath=PluginControllerProperties.NO_VALUE
		//springConfigLocation="classpath:org/creditsms/plugins/paymentview/paymentview-spring-hibernate.xml",
		//hibernateConfigPath="classpath:org/creditsms/plugins/paymentview/paymentview.hibernate.cfg.xml"
		)
public class PaymentViewPluginController extends BasePluginController implements IncomingMessageListener {
	
//>	CONSTANTS
	/** Filename and path of the XML for the PaymentView tab */
	private static final String XML_PAYMENT_VIEW_TAB = "/ui/plugins/paymentview/paymentViewTab.xml";
	
//> INSTANCE PROPERTIES
	/** The {@link #FrontlineSMS} instance that this plug-in is attached to */
	private FrontlineSMS frontlineController;

//> CONFIG METHODS
	/** @see net.frontlinesms.plugins.PluginController#init(FrontlineSMS, ApplicationContext) */
	public void init(FrontlineSMS frontlineController,	ApplicationContext applicationContext) throws PluginInitialisationException {
		this.frontlineController = frontlineController;
		this.frontlineController.addIncomingMessageListener(this);
	}

	/** @see net.frontlinesms.plugins.BasePluginController#initThinletTab(UiGeneratorController) */
	public Object initThinletTab(UiGeneratorController uiController) {
		PaymentViewThinletTabController tabController = new PaymentViewThinletTabController(this, uiController);
		
		Object paymentViewTab = uiController.loadComponentFromFile(XML_PAYMENT_VIEW_TAB, tabController);
		tabController.setTabComponent(paymentViewTab);
		
		return paymentViewTab;
	}

	/**
	 * @see net.frontlinesms.plugins.PluginController#deinit()
	 */
	public void deinit() {
		this.frontlineController.removeIncomingMessageListener(this);
	}

	/**
	 * Ensures that the incoming message is trapped and the necessary information
	 * is extracted i.e. transaction type, amount, sender and transaction code (if any)
	 * The above parameters may vary amongst service providers
	 */
	public void incomingMessageEvent(Message message) {		
	}

}