package org.creditsms.plugins.paymentview;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentServiceSmsProcessor {
//> CONSTANTS
    /** Tag for extracting/inserting the amount transacted */
    public static final String TG_AMOUNT = "<Amount>";
    /** Tag for extracting the first name of the sender/recipient */
    public static final String TG_FIRSTNAME = "<FirstName>";
    /** Tag for extracting the last name of the sender/recipient */
    public static final String TG_LASTNAME = "<LastName>";
    /** Tag for extracting the balance */
    public static final String TG_BALANCE = "<Balance>";
    /** Tag for extracting/inserting the phone number of the sender/recipient */
    public static final String TG_PHONENUMBER = "<PhoneNumber>";
    /** Tag for extracting the transaction id */
    public static final String TG_TRANSACTION_CODE = "<TransactionCode>";
    /** Tag for inserting the PIN Number */
    public static final String TG_PIN_NUMBER = "<PinNumber>";
	
//> PROPERTIES	
    /** SMS received on the connected modem */
    private String[] sourceMessage;
    /** Template SMS to be used for extracting the metadata*/
    private String[] templateSMS;
    /** Stores the indexes of the tags to be used for extracting information from the source message */
    private Hashtable<String, String> tagPositions = new Hashtable<String, String>();
    
//> CONSTRUCTOR
    public PaymentServiceSmsProcessor(String message, String confirmTextTemplate){
        //Assumption is that the source message is delimited using a _ (space)
        sourceMessage = message.split(" ");
				
        //Set the template message to be used
        String confirmText = confirmTextTemplate;
		
        templateSMS = confirmText.split(" ");
		
        //Build a map of the positions
        for(int i=0; i<templateSMS.length; i++){
            if(templateSMS[i].equals(PaymentServiceSmsProcessor.TG_AMOUNT))
                tagPositions.put(PaymentServiceSmsProcessor.TG_AMOUNT, Integer.toString(i));
            else if(templateSMS[i].equals(PaymentServiceSmsProcessor.TG_BALANCE))
                tagPositions.put(PaymentServiceSmsProcessor.TG_BALANCE, Integer.toString(i));
            else if(templateSMS[i].equals(PaymentServiceSmsProcessor.TG_FIRSTNAME))
                tagPositions.put(PaymentServiceSmsProcessor.TG_FIRSTNAME, Integer.toString(i));
            else if(templateSMS[i].equals(PaymentServiceSmsProcessor.TG_LASTNAME)) 
                tagPositions.put(PaymentServiceSmsProcessor.TG_LASTNAME, Integer.toString(i));
            else if(templateSMS[i].equals(PaymentServiceSmsProcessor.TG_PHONENUMBER))
                tagPositions.put(PaymentServiceSmsProcessor.TG_PHONENUMBER, Integer.toString(i));
            else if(templateSMS[i].equals(PaymentServiceSmsProcessor.TG_TRANSACTION_CODE))
                tagPositions.put(PaymentServiceSmsProcessor.TG_TRANSACTION_CODE, Integer.toString(i));
        }		
    }
	
    /**
     *  Gets the amount transacted from the confirmation text
     *   @return
     */
    public double getAmount(){
        return getNumber(PaymentServiceSmsProcessor.TG_AMOUNT);
    }
	
    /**
     * Retrives the balance from the confirmation tex
     *  @return
     */
    public double getBalance(){
        return getNumber(PaymentServiceSmsProcessor.TG_BALANCE);
    }
	
    /**
     *  Gets the {@link Client}'s phone number from the confirmation text
     *  @return
     */
    public String getPhoneNumber(){
        return getText(PaymentServiceSmsProcessor.TG_PHONENUMBER);
    }
	
    /**
     * Gets the {@link Client}'s first name from the confirmation text
     * @return
     */
    public String getFirstName(){
        return getText(PaymentServiceSmsProcessor.TG_FIRSTNAME);
    }
	
    /**
     * Gets the {@link Client}'s last name from the confirmation text
     * @return
     */
    public String getLastName(){
        return getText(PaymentServiceSmsProcessor.TG_LASTNAME);
    }
	
    /**
     * Gets the {@link Client}'s full name by concatenating the first name and last name
     * from the confirmation text
     * @return
     */
    public String getFullName(){
        return (getFirstName() + " " + getLastName());
    }
	
    /**
     * Extracts the transaction code from the confirmation text
     * @return
     */
    public String getTransactionCode(){
        return getText(PaymentServiceSmsProcessor.TG_TRANSACTION_CODE);
    }
	
    /**
     * Extracts the numerical value from the sourceMessage using the specified tag
     * @param tag
     * @return
     */
    private double getNumber(String tag){
        // Retrieve the position referenced by the tag
        String strValue = tagPositions.get(tag);
		
        // Get the string in the source message associated with the retrieved position
        String value = sourceMessage[Integer.parseInt(strValue)];
		
        // Regular expression match
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(value);
		
        // Buffer to store the fetched numerical values 
        StringBuffer numberBuffer = new StringBuffer();
		
        // Continuously fetch the numbers and push them to the buffer 
        while(matcher.find()){
            if(numberBuffer.length() > 0)
		        numberBuffer.append(".");
		    
            numberBuffer.append(matcher.group());
        }
		
        // Return numerical value
        return (numberBuffer.length() > 0) ? Double.parseDouble(numberBuffer.toString()) : 0;
	}
	
    /**
     * Extracts the text value from the source message using the specified tag
     * @param tag
     * @return
     */
    private String getText(String tag){
        String strIndex = tagPositions.get(tag);
        return sourceMessage[Integer.parseInt(strIndex)];
    }
	
}
