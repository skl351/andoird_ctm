package com.ayi.entity;

public class Version {
    private String V_last_ver;
    private String V_unactivated_ver;
    private String V_APK_path;
    private String V_APK_name;
    private String V_meno;

    public String getV_last_ver() {
        return V_last_ver == null ? "" : V_last_ver;
    }

    public void setV_last_ver(String v_last_ver) {
        V_last_ver = v_last_ver == null ? "" : v_last_ver;
    }

    public String getV_unactivated_ver() {
        return V_unactivated_ver == null ? "" : V_unactivated_ver;
    }

    public void setV_unactivated_ver(String v_unactivated_ver) {
        V_unactivated_ver = v_unactivated_ver == null ? "" : v_unactivated_ver;
    }

    public String getV_APK_path() {
        return V_APK_path == null ? "" : V_APK_path;
    }

    public void setV_APK_path(String v_APK_path) {
        V_APK_path = v_APK_path == null ? "" : v_APK_path;
    }

    public String getV_APK_name() {
        return V_APK_name == null ? "" : V_APK_name;
    }

    public void setV_APK_name(String v_APK_name) {
        V_APK_name = v_APK_name == null ? "" : v_APK_name;
    }

    public String getV_meno() {
        return V_meno == null ? "" : V_meno;
    }

    public void setV_meno(String v_meno) {
        V_meno = v_meno == null ? "" : v_meno;
    }

    @Override
    public String toString() {
        return "Version{" +
                "V_last_ver='" + V_last_ver + '\'' +
                ", V_unactivated_ver='" + V_unactivated_ver + '\'' +
                ", V_APK_path='" + V_APK_path + '\'' +
                ", V_APK_name='" + V_APK_name + '\'' +
                ", V_meno='" + V_meno + '\'' +
                '}';
    }
}
