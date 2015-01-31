/**
 * 
 */
package org.sharetask.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.sharetask.api.StatisticsService;
import org.sharetask.api.dto.StatisticsDataDTO;
import org.sharetask.api.dto.StatisticsOverviewDTO;
import org.sharetask.data.ServiceUnitTest;

/**
 * The Class StatisticsServiceTest.
 *
 * @author Michal Bocek
 */
public class StatisticsServiceTest extends ServiceUnitTest {

	@Inject
	private StatisticsService statisticsService;
	
	@Test
	public void testGetOverviewTotalUsersCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsTotal = overview.getStatisticsTotal();

		assertThat(statisticsTotal.getUsersCount())
				.as("Total count of registered users should be")
				.isGreaterThan(0);
	}
	
	@Test
	public void testGetOverviewTotalWorkspacesCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsTotal = overview.getStatisticsTotal();
		
		assertThat(statisticsTotal.getWorkspacesCount())
				.as("Total count of workspaces should be")
				.isGreaterThan(0);
	}
	
	@Test
	public void testGetOverviewTotalTasksCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsTotal = overview.getStatisticsTotal();
		
		assertThat(statisticsTotal.getTasksCount())
				.as("Total count of tasks should be")
				.isGreaterThan(0);
	}
	
	@Test
	public void testGetOverviewLastWeekUsersCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastWeek = overview.getStatisticsPerLastWeek();

		assertThat(statisticsLastWeek.getUsersCount())
				.as("Last week count of registered users should be")
				.isEqualTo(0);
	}
	
	@Test
	public void testGetOverviewLastWeekWorkspacesCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastWeek = overview.getStatisticsPerLastWeek();
		
		assertThat(statisticsLastWeek.getWorkspacesCount())
				.as("Last week count of workspaces should be")
				.isEqualTo(0);
	}
	
	@Test
	public void testGetOverviewLastWeekTasksCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastWeek = overview.getStatisticsPerLastWeek();
		
		assertThat(statisticsLastWeek.getTasksCount())
				.as("Last week count of tasks should be")
				.isGreaterThan(0);
	}

	@Test
	public void testGetOverviewLastYearUsersCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastYear = overview.getStatisticsPerLastYear();

		assertThat(statisticsLastYear.getUsersCount())
				.as("Last year count of registered users should be")
				.isEqualTo(0);
	}
	
	@Test
	public void testGetOverviewLastYearWorkspacesCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastYear = overview.getStatisticsPerLastYear();
		
		assertThat(statisticsLastYear.getWorkspacesCount())
				.as("Last yaer count of workspaces should be")
				.isEqualTo(0);
	}
	
	@Test
	public void testGetOverviewLastYearTasksCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastYear = overview.getStatisticsPerLastYear();
		
		assertThat(statisticsLastYear.getTasksCount())
				.as("Last year count of tasks should be")
				.isGreaterThan(0);
	}
	
	@Test
	public void testGetOverviewLastDayUsersCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastDay = overview.getStatisticsPerLastDay();

		assertThat(statisticsLastDay.getUsersCount())
				.as("Last day count of registered users should be")
				.isEqualTo(0);
	}
	
	@Test
	public void testGetOverviewLastDayWorkspacesCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastDay = overview.getStatisticsPerLastDay();
		
		assertThat(statisticsLastDay.getWorkspacesCount())
				.as("Last day count of workspaces should be")
				.isEqualTo(0);
	}
	
	@Test
	public void testGetOverviewLastDayTasksCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastDay = overview.getStatisticsPerLastDay();
		
		assertThat(statisticsLastDay.getTasksCount())
				.as("Last day count of tasks should be")
				.isGreaterThan(0);
	}	
	
	@Test
	public void testGetOverviewLastHourUsersCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastHour = overview.getStatisticsPerLastHour();

		assertThat(statisticsLastHour.getUsersCount())
				.as("Last hour count of registered users should be")
				.isEqualTo(0);
	}
	
	@Test
	public void testGetOverviewLastHourWorkspacesCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastHour = overview.getStatisticsPerLastHour();
		
		assertThat(statisticsLastHour.getWorkspacesCount())
				.as("Last hour count of workspaces should be")
				.isEqualTo(0);
	}
	
	@Test
	public void testGetOverviewLastHourTasksCount() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsLastHour = overview.getStatisticsPerLastHour();
		
		assertThat(statisticsLastHour.getTasksCount())
				.as("Last hour count of tasks should be")
				.isEqualTo(1);
	}
}
