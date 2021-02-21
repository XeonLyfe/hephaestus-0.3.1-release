/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;
import me.zeroeightsix.kami.command.syntax.parsers.ModuleParser;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.util.Wrapper;

public class BindCommand
extends Command {
    public BindCommand() {
        super("bind", new ChunkBuilder().append("module", true, new ModuleParser()).append("key", true).build());
    }

    @Override
    public void call(String[] args) {
        if (args.length == 1) {
            Command.sendChatMessage("Please specify a module.");
            return;
        }
        String rkey = args[1];
        String module = args[0];
        Module m = ModuleManager.getModuleByName(module);
        if (m == null) {
            BindCommand.sendChatMessage("Unknown module '" + module + "'!");
            return;
        }
        if (rkey == null) {
            BindCommand.sendChatMessage(m.getName() + " is bound to &b" + m.getBindName());
            return;
        }
        int key = Wrapper.getKey(rkey);
        if (rkey.equalsIgnoreCase("none")) {
            key = -1;
        }
        if (key == 0) {
            BindCommand.sendChatMessage("Unknown key '" + rkey + "'!");
            return;
        }
        m.getBind().setKey(key);
        BindCommand.sendChatMessage("Bind for &b" + m.getName() + "&r set to &b" + rkey.toUpperCase());
    }
}

