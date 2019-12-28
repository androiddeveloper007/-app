package com.zp.mobile91q.pgmdetail;

public class CancelableRunnable implements Runnable{
    private volatile boolean cancelled;

    @Override
    public void run() {
        while(!cancelled) {

        }
    }

    public void cancel() {
        cancelled = true;
    }
}
