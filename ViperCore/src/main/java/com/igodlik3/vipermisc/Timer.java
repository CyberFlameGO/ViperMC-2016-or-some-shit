package com.igodlik3.vipermisc;

public interface Timer
{
    boolean isActive();
    
    long getLeftTime();
    
    long getTimerEnd();
    
    void setTimerEnd(final long p0);
    
    String toString();
}
