package com.bitlab.connect.data;

import com.bitlab.message.Version;
import com.bitlab.message.data.IPv6;

public class StateBundle {

    private final String ip;
    private final int port;
    private Version version;
    private final long connectionTry;//czas próby połączenia
    private boolean timeout;
    private boolean success;
    private Throwable exception;
    private TypeOfAction typeOfAction;

    

    public StateBundle (final String ip, final int port, final long connection_try) {
        this.ip = ip;
        this.port = port;
        this.connectionTry = connection_try;
        timeout = false;
        success = false;
        typeOfAction = TypeOfAction.NOTSET;
    }

    public StateBundle (final IPv6 ip, final  int port, final long connectionTry) {
        this(ip.toString(), port, connectionTry);
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public Version getVersion() {
        return version;
    }

    public long getConnectionTry() {
        return connectionTry;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getException() {
        if(exception != null)
            return exception.getMessage();
        else
            return null;
    }
    
    public TypeOfAction getTypeOfAction() {
        return this.typeOfAction;
    }

    public void setTypeOfAction(TypeOfAction typeOfAction) {
        this.typeOfAction = typeOfAction;
    }
}
