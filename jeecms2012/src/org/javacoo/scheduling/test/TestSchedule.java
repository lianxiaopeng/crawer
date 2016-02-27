package org.javacoo.scheduling.test;


import org.javacoo.scheduling.core.Scheduler;
import org.javacoo.scheduling.core.SchedulerTask;
import org.javacoo.scheduling.impl.ScheduleParamBean;
import org.javacoo.scheduling.impl.SimpleScheduleIterator;
/**
 * 测试
 * @author javacoo
 * @since 2011-11-03
 */
public class TestSchedule {
	private final Scheduler scheduler = new Scheduler();
	
	private final ScheduleParamBean scheduleParamBean;

	public TestSchedule(final ScheduleParamBean scheduleParamBean) {
		this.scheduleParamBean = scheduleParamBean;
	}

	public void start() {
		scheduler.schedule(new SchedulerTask() {
			public void run() {
				execute();
			}
			private void execute() {
				System.out.println("任务执行");
			}
		}, new SimpleScheduleIterator(scheduleParamBean));
	}

	public static void main(String[] args) {
		
		ScheduleParamBean scheduleParamBean = new ScheduleParamBean();
		//scheduleParamBean.setWeekOfMonth(1);
		scheduleParamBean.setDayOfWeek(4);
		scheduleParamBean.setDayOfMonth(4);
		scheduleParamBean.setHourOfDay(22);
		scheduleParamBean.setMinute(59);
		scheduleParamBean.setSecond(0);
		TestSchedule test = new TestSchedule(scheduleParamBean);
		test.start();

	}

}
