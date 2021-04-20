package hu.mobilalkfejl.clinicalissuelog;

public class Qualification {
    private String id;
    private String code;
    private String issuer;

    public Qualification(String code, String issuer) {
        this.code = code;
        this.issuer = issuer;
    }

    public Qualification(){}

    public String _getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
