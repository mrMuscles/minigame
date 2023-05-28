package com.professoreno.mc.minigame.commands;

import com.professoreno.mc.minigame.utilities.withercontroller.CustomWitherController;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WitherControllerCommand implements TabExecutor {
    static final List<String> methodList = Arrays.asList("debug","debugstop","rotate","rotatestop","targetnull",
            "targetnullstop","secondphase","setnoai","invulticks","clean","upsidedown","rightsideup", "setname");
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.isOp()) {

                // /c [witherindex] [method] [methodargs]

                int witherIndex;

                boolean doesMethodTakeInts = false;
                boolean doesMethodTakeString = false;

                String witherIndexStr;
                String methodCall;
                String methodArgs = null;

                List<CustomWitherController> witherControllerList = CustomWitherController.customWitherControllers;

                if (args.length < 2 | args.length > 3 | witherControllerList.size() == 0) return false;

                witherIndexStr = args[0];
                methodCall = args[1];

                if (args.length == 3) {
                    methodArgs = args[2];
                    // checks if index can be parsed
                    if (!NumberUtils.isParsable(witherIndexStr)) {
                        return false;
                    }
                    if (NumberUtils.isParsable(methodArgs)) {
                        doesMethodTakeInts = true;
                    }
                    else {
                        doesMethodTakeString = true;
                    }
                }

                witherIndex = Integer.parseInt(witherIndexStr);
                CustomWitherController witherController = witherControllerList.get(witherIndex);

                if (doesMethodTakeInts) {
                    // switch for methods that take ints
                    switch (Objects.requireNonNull(methodCall)) {
                        case "invulticks" -> witherController.invulTicks(Integer.parseInt(methodArgs));
                        default -> { return false; }
                    }
                    return true;

                } else if (doesMethodTakeString) {
                    // switch for methods that take strings
                    switch (Objects.requireNonNull(methodCall)) {
                        case "setname" -> witherController.setName(args[2]);
                        default -> { return false; }
                    }
                    return true;
                }

                // switch for methods that don't take either
                switch (Objects.requireNonNull(methodCall)) {
                    case "debug" -> witherController.debug();
                    case "debugstop" -> witherController.debugStop();
                    case "rotate" -> witherController.rotate();
                    case "rotatestop" -> witherController.rotateStop();
                    case "targetnull" -> witherController.targetingNull();
                    case "targetnullstop" -> witherController.targetingNullStop();
                    case "secondphase" -> witherController.secondPhase();
                    case "setnoai" -> witherController.noAi();
                    case "clean" -> witherController.cleanup();
                    case "upsidedown" -> witherController.upsidedown();
                    case "rightsideup" -> witherController.rightsideup();
                    case "setname" -> witherController.setName(args[2]);
                    default -> { return false; }
                }


                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int size = CustomWitherController.customWitherControllers.size();
        if (args.length == 2) {
            return methodList;
        }
        if (args.length == 1 && size != 0) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < CustomWitherController.customWitherControllers.size(); i++) {
                list.add(Integer.toString(i));
            }
            return list;
        }
        return null;
    }
}
