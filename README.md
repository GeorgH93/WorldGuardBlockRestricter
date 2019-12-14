# WorldGuardBlockRestricter
Allows to restrict the usage of certain blocks in WorldGuard regions.

This is a fork of the original [WorldGuard Block Restricter](https://dev.bukkit.org/projects/worldguard-block-restricter) from mewin, updated for newer versions of Minecraft.

## Description
WorldGuard Block Restricter adds six flags to WorldGuard regions:

- deny-blocks: a list of blocks that are not allowed to be placed or destroyed in this region
- allow-blocks: a list of blocks that are allowed to be placed and destroyed (overwrites deny-blocks)
- deny-place: only deny placing the specified blocks, but not breaking them
- allow-place: overwrite blocks specified by "deny-place"
- deny-break: only deny breaking the specified blocks, but not placing them
- allow-break: overwrite blocks specified by "deny-break"

## Usage
Simply use these flag as any other WorldGuard flag:
`/region flag restriction deny-blocks obsidian, cobblestone`

You can also use "any" to block any block placement/destruction. This is useful for whitelisting, because allow-blocks will overwrite this.

```
/region flag only-dirt deny-blocks any
/region flag only-dirt allow-blocks dirt
```

## Permissions
- `wgblockrestricter.ignore`: allows players to ignore block restrictions

## Requirements:
- WorldGuard 6.2 (other 6.x version might work)

## Credits:
I would like to thank mewin, the original author of [WorldGuard Block Restricter](https://dev.bukkit.org/projects/worldguard-block-restricter) for his work.
