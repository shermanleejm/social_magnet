import org.junit.jupiter.api.*;

import main.java.game.*;

public class PlotTest {
    @Test
    public void testgetName() {
        Plot test_plot = new Plot(1, 1, 1, "Plot for daysss", 100, 100001, 5, 5);

        Assertions.assertEquals("Plot for daysss", test_plot.getName());
    }

    @Test
    public void testgetID() {
        Plot test_plot = new Plot(1, 1, 1, "Plot for daysss", 100, 100001, 5, 5);

        Assertions.assertEquals(1, test_plot.getID());
    }

    @Test
    public void testgetUserID() {
        Plot test_plot = new Plot(1, 1, 1, "Plot for daysss", 100, 100001, 5, 5);

        Assertions.assertEquals(1, test_plot.getUserID());
    }

    @Test
    public void testgetPlotPosition() {
        Plot test_plot = new Plot(1, 1, 1, "Plot for daysss", 100, 100001, 5, 5);

        Assertions.assertEquals(1, test_plot.getPlotPosition());
    }

    @Test
    public void testgetStartTime() {
        Plot test_plot = new Plot(1, 1, 1, "Plot for daysss", 100, 100001, 5, 5);

        Assertions.assertEquals(100001, test_plot.getStartTime());
    }

    @Test
    public void testgetStatus() {
        Plot test_plot = new Plot(1, 1, 1, "Plot for daysss", 100, 100001, 5, 5);

        Assertions.assertEquals("growing", test_plot.getStatus());
    }

    @Test
    public void testgetStolenYield() {
        Plot test_plot = new Plot(1, 1, 1, "Plot for daysss", 100, 100001, 50, 5);

        Assertions.assertEquals(5, test_plot.getStolenYield());
    }

    @Test
    public void testupdateStatusWilted() {
        Plot test_plot = new Plot(1, 1, 1, "Plot for daysss", 100, 100001, 50, 5);

        test_plot.updateStatus(10);

        Assertions.assertEquals("wilted", test_plot.getStatus());
    }

    @Test
    public void testupdateStatusHarvest() {
        Plot test_plot = new Plot(1, 1, 1, "Plot for daysss", 100, 1586262579, 50, 5);

        test_plot.updateStatus(10);

        Assertions.assertEquals("wilted", test_plot.getStatus());
    }

    @Test
    public void testgetGrowthPercent() {
        Plot test_plot = new Plot(1, 1, 1, "Plot for daysss", 100, 100001, 50, 5);

        Assertions.assertEquals(100, test_plot.getGrowthPercent());
    }

    @Test
    public void testgetGrowthProgress() {
        Plot test_plot = new Plot(1, 1, 1, "Plot for daysss", 100, 100001, 50, 5);

        Assertions.assertEquals("  wilted  ", test_plot.getGrowthProgress());
    }

}