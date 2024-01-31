package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.domain.ReviewDate;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.REVIEW_CYCLE_NAME;
import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.START_DATE;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ReviewDateProviderServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    ReviewDateProviderService reviewDateProviderService;

    @Autowired
    ReviewDateRepository reviewDateRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    private static Stream<List<Integer>> createCycle() {
        return Stream.of(
                List.of(1, 2, 5, 7),
                List.of(4, 7, 20, 23, 25, 30, 50),
                List.of(1, 2, 4, 8, 16, 32),
                List.of(7, 14, 21, 28, 35, 42, 49, 56),
                List.of(7)
        );
    }

    @ParameterizedTest
    @MethodSource("createCycle")
    void 복습_주기와_시작_날짜로_복습_날짜들을_가져올_수_있다(List<Integer> cycle) {
        // given
        Long userId = 1L;
        for (Integer day : cycle) {
            reviewDateRepository.save(new ReviewDate(START_DATE.plusDays(day), userId));
        }

        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);

        // when
        List<ReviewDate> reviewDates = reviewDateProviderService.provideReviewDates(reviewCycle, START_DATE);

        // then
        assertThat(reviewDates).hasSize(cycle.size());
        for (ReviewDate reviewDate : reviewDates) {
            assertThat(reviewDateRepository.existsById(reviewDate.getId())).isTrue();
        }
    }

    @ParameterizedTest
    @MethodSource("createCycle")
    void 복습_주기와_시작_날짜로_복습_날짜를_가져올_때_존재하지_않는_복습_날짜는_생성해서_가져올_수_있다(List<Integer> cycle) {
        // given
        Long userId = 1L;
        for (int i = 0; i < cycle.size(); i += 2) {
            Integer day = cycle.get(i);
            reviewDateRepository.save(new ReviewDate(START_DATE.plusDays(day), userId));
        }

        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);

        // when
        List<ReviewDate> reviewDates = reviewDateProviderService.provideReviewDates(reviewCycle, START_DATE);

        // then
        assertThat(reviewDates).hasSize(cycle.size());
        for (ReviewDate reviewDate : reviewDates) {
            assertThat(reviewDateRepository.existsById(reviewDate.getId())).isTrue();
        }
    }

    @ParameterizedTest
    @MethodSource("createCycle")
    void 복습_주기와_시작_날짜로_복습_날짜들을_생성_후_가져올_수_있다(List<Integer> cycle) {
        // given
        Long userId = 1L;
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);

        // when
        List<ReviewDate> reviewDates = reviewDateProviderService.provideReviewDates(reviewCycle, START_DATE);

        // then
        assertThat(reviewDates).hasSize(cycle.size());
        for (ReviewDate reviewDate : reviewDates) {
            assertThat(reviewDateRepository.existsById(reviewDate.getId())).isTrue();
        }
    }

    @ParameterizedTest
    @MethodSource("createCycle")
    void provideReviewDates_메서드를_사용하면_복습_날짜들이_복습_주기에_맞게_DB에_존재해야_한다(List<Integer> cycle) {
        // given
        Long userId = 1L;
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);

        // when
        reviewDateProviderService.provideReviewDates(reviewCycle, START_DATE);

        // then
        for (Integer day : cycle) {
            LocalDate newDate = START_DATE.plusDays(day);
            assertThat(reviewDateRepository.findByDate(userId, newDate)).isPresent();
        }
    }

}
