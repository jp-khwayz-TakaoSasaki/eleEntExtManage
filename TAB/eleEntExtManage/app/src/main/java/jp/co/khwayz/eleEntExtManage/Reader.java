package jp.co.khwayz.eleEntExtManage;

public class Reader {
    private boolean connected = false;
    public void connect() {
        this.connected = true;
    }
    public void disconnect() {
        this.connected = false;
    }
    public boolean isConnected() {
        return this.connected;
    }
}
