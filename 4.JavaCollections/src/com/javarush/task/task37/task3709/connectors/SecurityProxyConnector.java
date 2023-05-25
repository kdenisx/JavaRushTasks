package com.javarush.task.task37.task3709.connectors;

import com.javarush.task.task37.task3709.security.SecurityChecker;
import com.javarush.task.task37.task3709.security.SecurityCheckerImpl;

public class SecurityProxyConnector implements Connector{

    private SecurityChecker securityChecker = new SecurityCheckerImpl();
    private SimpleConnector simpleConnector;

    public SecurityProxyConnector(String string) {
        this.simpleConnector = new SimpleConnector(string);
    }

    @Override
    public void connect() {
        if (securityChecker.performSecurityCheck()) {
            simpleConnector.connect();
        } else {
            System.out.println("Check failed!");
        }

    }
}
