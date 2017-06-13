package net.bean;

import javafx.beans.property.BooleanProperty;

import javafx.beans.property.SimpleBooleanProperty;

import javafx.beans.property.SimpleStringProperty;

import javafx.beans.property.StringProperty;

public class AMessage {
//名字 收录时间 文件大小 文件数 速度 人气

    private final BooleanProperty invited;
    private final StringProperty fileName;
    private final StringProperty slShiJian;
    private final StringProperty wjDaXiao;
    private final StringProperty wjShu;
    private final StringProperty SD;
    private final StringProperty RQ;
    private final StringProperty ciliLian;

    public AMessage(boolean b, String btname, String addtime, String fsize, String fnum, String speed, String Popularity, String attribute) {
        this.invited =new SimpleBooleanProperty(b);
        this.fileName = new SimpleStringProperty(btname);
        this.slShiJian = new SimpleStringProperty(addtime);
        this.wjDaXiao  = new SimpleStringProperty(fsize);
        this.wjShu  = new SimpleStringProperty(fnum);
        this.SD  = new SimpleStringProperty(speed);
        this.RQ  = new SimpleStringProperty(Popularity);
        this.ciliLian  =  new SimpleStringProperty(attribute);
    }

    public boolean isInvited() {
        return invited.get();
    }

    public void setInvited(boolean value) {
        invited.set(value);
    }

    public BooleanProperty invitedProperty() {
        return invited;
    }

    public String getFileName() {
        return fileName.get();
    }

    public void setFileName(String value) {
        fileName.set(value);
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public String getSlShiJian() {
        return slShiJian.get();
    }

    public void setSlShiJian(String value) {
        slShiJian.set(value);
    }

    public StringProperty slShiJianProperty() {
        return slShiJian;
    }

    public String getWjDaXiao() {
        return wjDaXiao.get();
    }

    public void setWjDaXiao(String value) {
        wjDaXiao.set(value);
    }

    public StringProperty wjDaXiaoProperty() {
        return wjDaXiao;
    }

    public String getWjShu() {
        return wjShu.get();
    }

    public void setWjShu(String value) {
        wjShu.set(value);
    }

    public StringProperty wjShuProperty() {
        return wjShu;
    }

    public String getSD() {
        return SD.get();
    }

    public void setSD(String value) {
        SD.set(value);
    }

    public StringProperty SDProperty() {
        return SD;
    }

    public String getRQ() {
        return RQ.get();
    }

    public void setRQ(String value) {
        RQ.set(value);
    }

    public StringProperty RQProperty() {
        return RQ;
    }

    public String getCiliLian() {
        return ciliLian.get();
    }

    public void setCiliLian(String value) {
        ciliLian.set(value);
    }

    public StringProperty ciliLianProperty() {
        return ciliLian;
    }

}
