# TickChunks
SpigotMC / Minecraft plugin that keeps ticking chunks

When all players move away further than the server viewing distance from a chunk then mobs in that chunk
including villagers will stop moving, and crops will stop growing. These activities depend on what is called
"random ticks" which are sent only to chunks within viewing distance of at least one player.

This plug-in allows you to mark chunks for which you would like those "random ticks" to continue being sent,
despite no player being near. For this, the plug-in introduces the "/tickchunks" (or /tc for short)
command. Used with the sub-command "keep" or "k" for short the command will mark the chunk the player
is currently in such that this chunk will keep receiving random ticks. Using with the sub-command "release"
or "r" for short, the player's current chunk will be released from this mechanism.

When used in conjunction with the "KeepChunks" plug-in (see https://www.spigotmc.org/resources/keepchunks.23307/)
this plug-in will automatically request that chunks marked with /tickchunks are also kept loaded using
the KeepChunks plug-in.

Technical note: this plug-in depends on a Spigot feature that is yet to be released with the standard Spigot
version. See https://hub.spigotmc.org/jira/browse/SPIGOT-3747 which describes the feature request, and
https://hub.spigotmc.org/stash/projects/SPIGOT/repos/spigot/pull-requests/78/overview which describes
the pull request that contains the Bukkit/CraftBukkit extensions required by this plug-in. Clone
https://donaldduck70@hub.spigotmc.org/stash/scm/~donaldduck70/spigot.git if you have access to the Spigot Hub,
or https://github.com/axeluhl/SPIGOT-3747.git for everyone else; grab the correct patches for SPIGOT-3747 for Bukkit and CraftBukkit, either from the corresponding Stash repositories at https://donaldduck70@hub.spigotmc.org/stash/scm/~donaldduck70/Bukkit.git and https://donaldduck70@hub.spigotmc.org/stash/scm/~donaldduck70/CraftBukkit.git, respectively (or get it from Github if you don't have a Stash account; see below) and checkout SPIGOT-3747,
download BuildTools.jar and run them. See also https://www.spigotmc.org/wiki/buildtools/.

If on windows, start with this:
```
    git config --global --replace-all core.autocrlf true
```
Then continue like this:
```
    mkdir <my-spigot-dir>
    cd <my-spigot-dir>
    git clone https://github.com/axeluhl/SPIGOT-3747.git Spigot
    git clone https://github.com/axeluhl/Bukkit.git
    git clone https://github.com/axeluhl/CraftBukkit.git
    wget "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar"
```
Also, on Windows, make sure not to use Cygwin for the following because the autocrlf setting from above
will cause scripts to contain CR characters which the Cygwin bash doesn't like.
```
    java -jar BuildTools.jar --dont-update
```

This will give a spigot-X.YY.Z.jar with the necessary patches in place in your ``<my-spigot-dir>`` directory.

To build this plug-in against the Spigot JAR create as per the instructions above clone this plug-ins Git
repository and import into Eclipse. Go to the project's "Java Build Path" settings and set the variable
``Spigot`` to your choice for ``<my-spigot-dir>`` above. The project should then build without errors because
the spigot-X.YY.Z.jar file that you build before should be found by Eclipse.

Next, double-click the ``createjar.jardesc`` file in the project. It will create the TickChunks.jar file in your
Eclipse workspace directory. Move the resulting ``TickChunks.jar`` file to your server's ``plugins/`` directory.
This is assuming that your server runs off the ``spigot-X.YY.Z.jar`` compiled above.

Feedback to spigot@homemp3.dyndns.org. Have fun.
