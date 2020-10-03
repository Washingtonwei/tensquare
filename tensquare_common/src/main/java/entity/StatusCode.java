package entity;

public class StatusCode {
    public static final int OK = 20000;//Success
    public static final int ERROR = 20001;//Failure
    public static final int LOGINERROR = 20002;//Username or password wrong
    public static final int ACCESSERROR = 20003;//Access denied
    public static final int REMOTEERROR = 20004;//RPC failure
    public static final int REPERROR = 20005;//Repetitive ops
}
