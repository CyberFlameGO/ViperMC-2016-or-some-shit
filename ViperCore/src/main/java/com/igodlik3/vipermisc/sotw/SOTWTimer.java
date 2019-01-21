package com.igodlik3.vipermisc.sotw;

import com.igodlik3.vipermisc.*;

public class SOTWTimer implements Timer
{
    private long timerEnd;

    @Override
    public boolean isActive() {
        return this.getTimerEnd() > System.currentTimeMillis();
    }
    
    @Override
    public long getLeftTime() {
        return this.getTimerEnd() - System.currentTimeMillis();
    }
    
    @Override
    public long getTimerEnd() {
        return this.timerEnd;
    }
    
    @Override
    public void setTimerEnd(final long timerEnd) {
        this.timerEnd = timerEnd;
    }
    
    @Override
    public String toString() {
        final long left = this.getLeftTime();
        final long totalSecs = left / 1000L;
        if (totalSecs >= 3600L) {
            return String.format("%02d:%02d:%02d", totalSecs / 3600L, totalSecs % 3600L / 60L, totalSecs % 60L);
        }
        return String.format("%02d:%02d", totalSecs / 60L, totalSecs % 60L);
    }
}
