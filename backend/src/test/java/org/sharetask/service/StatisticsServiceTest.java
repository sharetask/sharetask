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
	public void testGetOverview() throws Exception {
		StatisticsOverviewDTO overview = statisticsService.getOverview();
		StatisticsDataDTO statisticsTotal = overview.getStatisticsTotal();

		assertThat(statisticsTotal.getUsersCount())
				.as("Total count of registered users should be")
				.isGreaterThan(0);
		
		assertThat(statisticsTotal.getWorkspacesCount())
				.as("Total count of workspaces should be")
				.isGreaterThan(0);
		
		assertThat(statisticsTotal.getTasksCount())
				.as("Total count of tasks should be")
				.isGreaterThan(0);
	}
}
