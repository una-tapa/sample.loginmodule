/*******************************************************************************
   This is a sample JAAS LoginModule
 *******************************************************************************/
package com.ibm.ws.samples.lm; 

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 *
 */
public class SampleLoginModule implements LoginModule {

    private CallbackHandler callbackHandler;
    private boolean succeeded;

    /** {@inheritDoc} */
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> arg2, Map<String, ?> arg3) {
        this.callbackHandler = callbackHandler;

        System.out.println("DEBUG: This loginModule is doing nothing but printing what's passed in, and return succesful login");
        System.out.println("DEBUG: Initializing SampleLoginModule!!");
        if (subject != null) {
            System.out.println("DEBUG: Subject=" + subject.toString());
        }
        else {
            System.out.println("DEBUG: Input subject is null"); 
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean login() throws LoginException  {
        boolean succeeded = false;
        Callback[] callbacks;
        try {

            callbacks = getCallbacks(callbackHandler);
            String username = ((NameCallback) callbacks[0]).getName();
            char[] passwordChars = ((PasswordCallback) callbacks[1]).getPassword();

            System.out.println("username=" + username + " password=" + passwordChars.toString()); 


            if ("Admin1".equalsIgnoreCase(username) ) {
                System.out.println("DEBUG: Admin1 is trying to login. Make login fail");
                succeeded = false;
                throw new LoginException("Login failed for user:" + username); 
            }
            else {
                System.out.println("DEBUGF: The user is not Admin1. Let the user login.");
            }


        } catch (LoginException le) {
            System.out.println(exceptionDetails(le));
            throw le; 
        } catch (Exception ee) {
            //Other exception
            System.out.println(exceptionDetails(ee));
        }


        return succeeded;
    }

    public static String exceptionDetails(Throwable t) {
        StringBuffer sb = new StringBuffer("-- Error Stack ---");
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        t.printStackTrace(new java.io.PrintStream(baos));
        sb.append("\n");
        sb.append(baos);
        sb.append("--- END Error Stack ---");
        return sb.toString();
   }

    /** {@inheritDoc} */
    @Override
    public boolean commit() throws LoginException {
        if (succeeded == false) {
            System.out.println("DEBUG: do not commit"); 
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean abort() throws LoginException {
        // TODO Auto-generated method stub
        if (succeeded == false) {
            System.out.println("DEBUG: abort = true"); 
            return true; 
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean logout() throws LoginException {
        System.out.println("DEBUG: logout was called"); 
        // TODO Auto-generated method stub
        return false;
    }

    private Callback[] getCallbacks(CallbackHandler callbackHandler) throws IOException, UnsupportedCallbackException {
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("Username: ");
        callbacks[1] = new PasswordCallback("Password: ", false);

        callbackHandler.handle(callbacks);
        return callbacks;
    }
}
