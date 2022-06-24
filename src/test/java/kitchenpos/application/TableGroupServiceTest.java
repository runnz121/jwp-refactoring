package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @Test
    @DisplayName("주문 테이블을 단체로 엮는 테이블 그룹을 생성한다.")
    void createTableGroup() {
        // given
        final OrderTable orderTableOf5Guests = new OrderTable(1L, null, 5, true);
        final OrderTable orderTableOf3Guests = new OrderTable(2L, null, 3, true);
        final TableGroup tableGroup = new TableGroup(Arrays.asList(orderTableOf5Guests, orderTableOf3Guests));
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTableOf5Guests, orderTableOf3Guests));
        when(tableGroupDao.save(any())).thenReturn(tableGroup);
        // when
        final TableGroup actual = tableGroupService.create(new kitchenpos.dto.TableGroupRequest(Arrays.asList(1L, 2L)));
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getOrderTables()).containsExactly(orderTableOf5Guests, orderTableOf3Guests)
        );
    }

    @Test
    @DisplayName("엮을 테이블은 한 개 이하면 예외 발생")
    void invalidLessThen2OrderTable() {
        // given
        final kitchenpos.dto.TableGroupRequest tableGroupRequest = new kitchenpos.dto.TableGroupRequest(Arrays.asList(1L));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @Test
    @DisplayName("엮을 주문 테이블이 존재하지 않으면 예외 발생")
    void notExistOrderTableId() {
        // given
        final kitchenpos.dto.TableGroupRequest tableGroupRequest = new kitchenpos.dto.TableGroupRequest(Arrays.asList(1L, 2L));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @ParameterizedTest(name = "빈 테이블이거나 다른 테이블에 그룹화 되어 있으면 예외 발생")
    @MethodSource("invalidEmptyOrGroupingOrderTableParameter")
    void emptyOrGroupingOrderTable(List<OrderTable> orderTables) {
        // given
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));
        when(orderTableDao.findAllByIdIn(any())).thenReturn(orderTables);
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    public static Stream<Arguments> invalidEmptyOrGroupingOrderTableParameter() {
        // given
        final OrderTable groupingOrderTable = new OrderTable(1L, 2L, 5, false);
        final OrderTable notEmptyOrderTable = new OrderTable(2L, null, 3, false);

        return Stream.of(
                Arguments.of(
                        Arrays.asList(groupingOrderTable, groupingOrderTable)
                ),
                Arguments.of(
                        Arrays.asList(notEmptyOrderTable, notEmptyOrderTable)
                )
        );
    }

    @Test
    @DisplayName("그룹화된 테이블을 해제한다.")
    void ungroupGroupTable() {
        // given
        final OrderTable groupingOneOrderTable = new OrderTable(1L, 1L, 5, false);
        final OrderTable groupingTwoOrderTable = new OrderTable(2L, 1L, 3, true);
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(groupingOneOrderTable, groupingTwoOrderTable));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(any());
        // when && then
        tableGroupService.ungroup(1L);
    }

    @Test
    @DisplayName("식사가 완료되지 않은 상태에서 그룹화된 테이블을 해제시 예외 발생")
    void ungroupCookingAndMealGroupTable() {
        // given
        final OrderTable groupingOneOrderTable = new OrderTable(1L, 1L, 5, false);
        final OrderTable groupingTwoOrderTable = new OrderTable(2L, 1L, 3, true);
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(groupingOneOrderTable, groupingTwoOrderTable));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(1L));
    }
}
