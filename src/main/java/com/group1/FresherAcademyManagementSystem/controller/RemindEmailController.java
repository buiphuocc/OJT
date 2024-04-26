package com.group1.FresherAcademyManagementSystem.controller;

import com.group1.FresherAcademyManagementSystem.models.Reserved_Class;
import com.group1.FresherAcademyManagementSystem.models.Student;
import com.group1.FresherAcademyManagementSystem.services.EmailService;
import com.group1.FresherAcademyManagementSystem.services.ReserveClassService;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class RemindEmailController {

    private final ReserveClassService reserveClassService;
    private final EmailService emailService;
    private final TaskScheduler taskScheduler;

    @Autowired
    public RemindEmailController(ReserveClassService reserveClassService, EmailService emailService, TaskScheduler taskScheduler) {
        this.reserveClassService = reserveClassService;
        this.emailService = emailService;
        this.taskScheduler = taskScheduler;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleEmailsForStudents() {
        List<Reserved_Class> students = reserveClassService.getAllReservedStudentsForRemind();
        LocalDate currentDate = LocalDate.now();

        for (Reserved_Class student : students) {
            LocalDate joinDate = student.getStartDate();

            if (joinDate != null && joinDate.equals(currentDate)) {
                LocalDateTime joinDateTime = joinDate.atTime(0, 5);
                ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
                Instant joinInstant = joinDateTime.atZone(vietnamZone).toInstant();

                Duration duration = Duration.between(Instant.now(), joinInstant);
                long delaySeconds = duration.getSeconds();

                Runnable task = () -> {
                    sendEmailToStudent(student.getStudent());
                };

                Instant startTime = Instant.now().plusSeconds(delaySeconds);
                ScheduledFuture<?> future = taskScheduler.schedule(task, startTime);
            } 
        }
    }

    private void sendEmailToStudent(Student student) {
        String to = student.getEmail();
        String subject = "Fresher Academy nhớ bạn, " + student.getFullName() + " ơi";
        String text = "Chào " + student.getFullName() + ",\n"
                + "Chúc bạn một ngày tốt lành! Tôi là Admin của Fresher Academy.\n"
                + "Chúng tôi rất vui vì bạn đã quyết định bảo lưu tại Fresher Academy để giữ vững kiến thức và kỹ năng của mình. Hiện tại, tôi muốn nhắc nhở bạn rằng thời hạn bảo lưu của bạn sẽ kết thúc trong vòng một tháng nữa.\n"
                + "Để giúp bạn dễ dàng quay lại và tiếp tục hành trình học tập, chúng tôi đề xuất bạn nên liên hệ với "
                + "chúng tôi trước khi thời hạn bảo lưu hết hạn. Bằng cách này, chúng tôi có thể hỗ trợ bạn trong quá trình xếp lớp và đảm bảo rằng bạn sẽ có trải nghiệm học tập tốt nhất tại Fresher Academy.\n"
                + "Hãy liên hệ với chúng tôi qua email này. Chúng tôi sẽ sẵn lòng hỗ trợ bạn với mọi thắc mắc và yêu cầu của bạn.\n"
                + "Chân thành cảm ơn sự quan tâm và cam kết của bạn đối với chương trình học tại Fresher Academy. Chúng tôi mong sớm được gặp lại bạn và chia sẻ những kiến thức mới.\n"
                + "Trân trọng,\n"
                + "Admin Fresher Academy";
        emailService.sendEmailRemind(to, subject, text);
    }
    
//    private void cancelTaskForStudent(String studentId) {
//        ScheduledFuture<?> future = scheduledTasks.remove(studentId);
//        if (future != null) {
//            future.cancel(false);
//        }
//    }
}
