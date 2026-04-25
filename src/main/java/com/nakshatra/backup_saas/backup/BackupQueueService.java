package com.nakshatra.backup_saas.backup;

import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class BackupQueueService {
    private final BlockingQueue<Long> queue = new LinkedBlockingQueue<>();

    public void enqueue(Long clientDbId) {
        queue.offer(clientDbId);
    }

    public Long dequeue() throws InterruptedException {
        return queue.take();
    }
}
