package me.hulipvp.chambers.map.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MapLevel {
    NORMAL("Normal", false, true),
    HIGH("High", true, true),
    AWESOME("Awesome", false, true);

    private String name;
    private boolean addTo; // if all players want, they can vote for.
    private boolean vip;
}
