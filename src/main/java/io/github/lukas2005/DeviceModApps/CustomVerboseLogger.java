package io.github.lukas2005.DeviceModApps;

import org.apache.openjpa.lib.log.Log;

public class CustomVerboseLogger implements Log {
    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isFatalEnabled() {
        return true;
    }

    @Override
    public void trace(Object o) {
        if (o instanceof Throwable) {
            ((Throwable)o).printStackTrace();
        } else {
            System.out.println(o);
        }
    }

    @Override
    public void trace(Object o, Throwable t) {
        System.out.println(o);
        t.printStackTrace();
    }

    @Override
    public void info(Object o) {
        System.out.println(o);
    }

    @Override
    public void info(Object o, Throwable t) {
        System.out.println(o);
        t.printStackTrace();
    }

    @Override
    public void warn(Object o) {
        System.out.println(o);
    }

    @Override
    public void warn(Object o, Throwable t) {
        System.out.println(o);
        t.printStackTrace();
    }

    @Override
    public void error(Object o) {
        System.err.println(o);
    }

    @Override
    public void error(Object o, Throwable t) {
        System.err.println(o);
        t.printStackTrace();
    }

    @Override
    public void fatal(Object o) {
        System.err.println(o);
    }

    @Override
    public void fatal(Object o, Throwable t) {
        System.err.println(o);
        t.printStackTrace();
    }
}
