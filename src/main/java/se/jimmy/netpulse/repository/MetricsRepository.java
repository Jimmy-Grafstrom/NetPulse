package se.jimmy.netpulse.repository;

import org.springframework.stereotype.Repository;
import se.jimmy.netpulse.model.NetworkMetric;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Repository
public class MetricsRepository {

    private final Deque<NetworkMetric> history = new ConcurrentLinkedDeque<>();
    private static final int MAX_HISTORY_SIZE = 100;

    public void add(NetworkMetric metric) {
        history.addLast(metric);
        while (history.size() > MAX_HISTORY_SIZE) {
            history.pollFirst();
        }
    }

    public List<NetworkMetric> getHistory() {
        return new ArrayList<>(history);
    }

    public NetworkMetric getLatest() {
        return history.peekLast();
    }
}
