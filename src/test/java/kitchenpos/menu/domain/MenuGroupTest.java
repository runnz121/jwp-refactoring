package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuGroupExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 그룹 테스트")
class MenuGroupTest {
    @Test
    void 동등성_테스트() {
        assertEquals(new MenuGroup("양식"), new MenuGroup("양식"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이름이_null이거나_빈_문자열이면_메뉴_그룹_객체를_생성할_수_없음(String name) {
        assertThatThrownBy(() -> {
            new MenuGroup(name);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuGroupExceptionCode.REQUIRED_NAME.getMessage());
    }
}
