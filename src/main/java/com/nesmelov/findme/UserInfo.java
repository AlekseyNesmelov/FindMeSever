package com.nesmelov.findme;

public class UserInfo {

    private boolean mVisibility;
    private Position mPosition;
    
    public UserInfo(final boolean visibility, final Position position) {
        mVisibility = visibility;
        mPosition = position;
    }
    
    public boolean getVisibility() {
        return mVisibility;
    }
    
    public Position getPosition() {
        return mPosition;
    }
    
    public void setVisible(final boolean visible) {
        mVisibility = visible;
    }
}  
