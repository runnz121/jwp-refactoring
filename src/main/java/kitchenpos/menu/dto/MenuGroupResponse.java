package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {
    private Long id;
    private String name;

    private MenuGroupResponse() {
    }

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<MenuGroupResponse> toList(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
