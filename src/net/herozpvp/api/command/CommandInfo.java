package net.herozpvp.api.command;


public @interface CommandInfo {
    String command();
    String description();
    boolean op();
    boolean player();
}
