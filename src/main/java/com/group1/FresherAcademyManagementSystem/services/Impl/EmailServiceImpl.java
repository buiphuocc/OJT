package com.group1.FresherAcademyManagementSystem.services.Impl;

import com.group1.FresherAcademyManagementSystem.dtos.emailDTO.EmailTemplateDetailDto;
import com.group1.FresherAcademyManagementSystem.dtos.emailDTO.ReservationEmailPreviewDTO;
import com.group1.FresherAcademyManagementSystem.dtos.emailDTO.*;
import com.group1.FresherAcademyManagementSystem.dtos.student_edit_dto.EmailTemplateDto2;
import com.group1.FresherAcademyManagementSystem.exceptions.StudentNotFoundException;
import com.group1.FresherAcademyManagementSystem.models.*;
import com.group1.FresherAcademyManagementSystem.repositories.*;
import com.group1.FresherAcademyManagementSystem.services.*;
import com.group1.FresherAcademyManagementSystem.token.Token;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailSendRepository emailSendRepository;

    @Autowired
    private EmailSendStudentRepository sendStudentRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthImplement authImplement;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassEntityRepository classRepository;

    @Autowired
    private ReserveClassRepository reserveClassRepository;

    @Override
    public void sendEmailToAStudent(Long idTemplate, String idStudent, HttpServletRequest request) {

        Student student = studentRepository.findById(idStudent).orElseThrow(() -> new RuntimeException("Student not found"));
        EmailTemplate emailTemp = emailTemplateRepository.findById(idTemplate).orElseThrow(() -> new RuntimeException("Template Not Found!"));

        UserEntity user = getUserInfo(request);

        String content = null;

        if (emailTemp.getUseDear()) {

            content = "<p>Dear " + student.getFullName() + "</p> " +
                    emailTemp.getContent() + "<p>" + user.getFullName() + "</p>";
            sendEmailWithHtmlTemplate(student.getEmail(), emailTemp.getName(), content);
        } else {
            content = emailTemp.getContent() + "<p>" + user.getFullName() + "</p>";
            sendEmailWithHtmlTemplate(student.getEmail(), emailTemp.getName(), content);
        }

        saveHistoryEmailToAStudent(user, emailTemp, "Send to " + student.getEmail(), student);
    }

    public void sendEmailWithHtmlTemplate(String to, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setFrom("minhnhut9a8@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private SimpleMailMessage createEmailMessage(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        return message;
    }

    @Override
    public ReservationEmailPreviewDTO previewMail(Long idTemplate, String idStudent, HttpServletRequest request) {

        Student student = studentRepository.findById(idStudent).orElseThrow(() -> new RuntimeException("Student not found"));
        EmailTemplate template = emailTemplateRepository.findById(idTemplate).orElseThrow(() -> new RuntimeException("Template Not Found!"));
        UserEntity user = getUserInfo(request);


        if (template.getUseDear()) {
            return preview(template, template.getName(), student.getEmail(), "Dear " + student.getFullName() + " " + template.getContent() + " " + user.getFullName());

        } else {
            return preview(template, template.getName(), student.getEmail(), template.getContent() + " " + user.getFullName());

        }

    }

    private UserEntity getUserInfo(HttpServletRequest request) {

        String token = authImplement.extractTokenFromHeader(request);

        final Token accessToken = tokenRepository.findByToken(token).orElse(null);
        if (accessToken == null) {
            throw new RuntimeException("Invalid JWT token");
        }

        String username = jwtService.extractUsername(token);

        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public void sendReservationEmailForAll(Long idTemplate, HttpServletRequest request) {

        EmailTemplate emailTemp = emailTemplateRepository.findById(idTemplate).orElseThrow(() -> new RuntimeException("Template Not Found!"));

        List<Reserved_Class> reservedClassList = reserveClassRepository.findAllReservedClasses();
        List<Student> students = new ArrayList<>();

        for (Reserved_Class sl : reservedClassList) {
            students.add(sl.getStudent());
        }

        UserEntity user = getUserInfo(request);
        String content = null;

        if (emailTemp.getUseDear()) {
            for (Student x : students) {
                content = "<p>Dear " + x.getFullName() + "</p> " +
                        emailTemp.getContent() + "<p>" + user.getFullName() + "</p>";
                sendEmailWithHtmlTemplate(x.getEmail(), emailTemp.getName(), content);
            }
        } else {
            for (Student x : students) {
                content = emailTemp.getContent() + "<p>" + user.getFullName() + "</p>";
                sendEmailWithHtmlTemplate(x.getEmail(), emailTemp.getName(), content);
            }
        }


        saveHistoryEmail(user, emailTemp, "Send email to all student", students);
    }

    @Override
    public void saveHistoryEmail(UserEntity user, EmailTemplate emailTemp, String content, List<Student> students) {

        EmailSend emailSends = new EmailSend();
        List<EmailSend_Student> emailSendStudentList = new ArrayList<EmailSend_Student>();

        emailSends.setUser(user);
        emailSends.setEmailTemplate(emailTemp);
        long currentTimeMillis = System.currentTimeMillis();
        emailSends.setSendDate(new Date(currentTimeMillis));
        emailSends.setAction(content);

        for (Student s : students) {
            EmailSend_Student emailSendStudent = new EmailSend_Student();
            emailSendStudent.setStudent(s);
            emailSendStudentList.add(emailSendStudent);
        }

        emailSends.setEmailSend_students(emailSendStudentList);
        EmailSend save = emailSendRepository.save(emailSends);

        for (EmailSend_Student es : emailSendStudentList) {
            es.setEmailSend(save);
            sendStudentRepository.save(es);
        }
    }

    @Override
    public void saveHistoryEmailToAStudent(UserEntity user, EmailTemplate emailTemp, String content, Student student) {
        EmailSend emailSends = new EmailSend();
        List<EmailSend_Student> emailSendStudentList = new ArrayList<EmailSend_Student>();

        emailSends.setUser(user);
        emailSends.setEmailTemplate(emailTemp);
        long currentTimeMillis = System.currentTimeMillis();
        emailSends.setSendDate(new Date(currentTimeMillis));
        emailSends.setAction(content);

        EmailSend_Student emailSendStudent = new EmailSend_Student();
        emailSendStudent.setStudent(student);


        emailSends.setEmailSend_students(emailSendStudentList);
        EmailSend save = emailSendRepository.save(emailSends);

        emailSendStudent.setEmailSend(save);
        sendStudentRepository.save(emailSendStudent);

    }


    @Override
    public ReservationEmailPreviewDTO previewListMail(Long idTemplate, HttpServletRequest request) {

        EmailTemplate template = emailTemplateRepository.findById(idTemplate).orElseThrow(() -> new RuntimeException("Template Not Found!"));
        UserEntity user = getUserInfo(request);

        if (template.getUseDear()) {
            return preview(template, template.getName(), "All", "Dear All, " + template.getContent() + " " + user.getFullName());
        } else {
            return preview(template, template.getName(), "All", template.getContent() + " " + user.getFullName());

        }
    }

    @Override
    public void informEmailReservation(Long idTemplate, String studentId) {
        EmailTemplate emailTemplate1 = emailTemplateRepository.findById(idTemplate).orElseThrow(() -> new RuntimeException("Template Not Found!"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException("Student not found !"));

        SimpleMailMessage message = createEmailMessage(student.getEmail(), emailTemplate1.getName(), emailTemplate1.getContent());
        mailSender.send(message);

    }

    @Override
    public void saveHistoryEmailSend(List<EmailSend> emailSends) {
        emailSendRepository.saveAll(emailSends);
    }

    @Override
    public void createEmailTemplate(com.group1.FresherAcademyManagementSystem.dtos.emailDTO.EmailTemplateDto emailTemplateDto, HttpServletRequest request) {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setName(emailTemplateDto.getNameTemlate());
        emailTemplate.setCategory(emailTemplateDto.getEmailTemplateArrageDto().getCategory());
        emailTemplate.setType(emailTemplateDto.getEmailTemplateArrageDto().getType());
        emailTemplate.setDescription(emailTemplateDto.getDescription());
        UserEntity user = getUserInfo(request);
        emailTemplate.setUserCreate(user);
        emailTemplate.setUserUpdate(user);
        emailTemplate.setUseDear(false);

        EmailTemplate check = emailTemplateRepository.findByName(emailTemplate.getName());
        if (check != null) {
            throw new IllegalArgumentException("Template Name is already exist !");
        } else {
            emailTemplateRepository.save(emailTemplate);
        }
    }

    @Override
    public EmailTemplateDetailDto viewDetail(Long idTemplate) {
        EmailTemplate emailTemplate = emailTemplateRepository.findById(idTemplate).orElseThrow(() -> new StudentNotFoundException("Can't find template"));
        return new EmailTemplateDetailDto(
                emailTemplate.getName(),
                emailTemplate.getDescription(),
                emailTemplate.getCategory(),
                emailTemplate.getCreateDate(),
                emailTemplate.getUserCreate().getFullName(),
                emailTemplate.getUseDear(),
                emailTemplate.getContent(),
                emailTemplate.getSubject(),
                emailTemplate.getType()
        );
    }

    @Override
    public EmailTemplateDetailDto updateDetail(HttpServletRequest request, Long idTemplate, EmailTemplateDetailDto emailTemplateDetailDto) {
        UserEntity user = getUserInfo(request);
        EmailTemplate emailTemplate = emailTemplateRepository.findById(idTemplate).orElseThrow(() -> new NoSuchElementException("Can't find template"));
        emailTemplate.setName(emailTemplateDetailDto.getEmailName());
        emailTemplate.setCreateDate(emailTemplateDetailDto.getCreatedOn());
        emailTemplate.setDescription(emailTemplateDetailDto.getDescription());
        emailTemplate.setCategory(emailTemplateDetailDto.getCategory());
        emailTemplate.setUseDear(emailTemplateDetailDto.getUseDear());
        emailTemplate.setContent(emailTemplateDetailDto.getContent());
        emailTemplate.setUserUpdate(user);

        EmailTemplate saved = emailTemplateRepository.save(emailTemplate);

        return mapToDetailDto(saved);
    }

    private ReservationEmailPreviewDTO preview(EmailTemplate template, String name, String receiver, String content) {

        ReservationEmailPreviewDTO previewDTO = modelMapper.map(template, ReservationEmailPreviewDTO.class);

        previewDTO.setTemplateName(name);
        previewDTO.setSender("minhnhut9a8@gmail.com");
        previewDTO.setSubject(name);
        previewDTO.setReceiver(receiver);
        previewDTO.setBodyText(content);

        return previewDTO;
    }

    @Override
    public void sendEmailRemind(String to, String subject, String text) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("khangbdse173094@fpt.edu.vn");
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);

        mailSender.send(email);
    }

    private List<EmailTemplateDto2> mapToDto(List<EmailTemplate> emailTemplates) {
        return emailTemplates.stream()
                .sorted(Comparator.comparing(EmailTemplate::getId))
                .map(item -> {
                    EmailTemplateDto2 dto = modelMapper.map(item, EmailTemplateDto2.class);

                    dto.setId(item.getId().toString());
                    dto.setName(item.getName());
                    dto.setDescription(item.getDescription());
                    dto.setCategories(item.getCategory());
                    dto.setApplyTo(item.getType());
                    if (item.getCreateDate() != null) {
                        dto.setCreateDate(item.getCreateDate().toString());
                    } else {
                        dto.setCreateDate(null);
                    }

                    return dto;
                }).collect(Collectors.toList());
    }

    private EmailTemplateDetailDto mapToDetailDto(EmailTemplate emailTemplate) {
        return new EmailTemplateDetailDto(
                emailTemplate.getName(),
                emailTemplate.getDescription(),
                emailTemplate.getCategory(),
                emailTemplate.getCreateDate(),
                emailTemplate.getUserCreate().getFullName(),
                emailTemplate.getUseDear(),
                emailTemplate.getContent(),
                emailTemplate.getSubject(),
                emailTemplate.getType()
        );
    }

    @Override
    public Page<EmailTemplateDto2> emailByFilter(int pageNumber, int pageSize, String sortField, String sortDir, String keyword, String[] types, String[] categories) {
        boolean student = false;
        boolean trainer = false;
        boolean inform = false;
        boolean remind = false;
        boolean score = false;
        boolean reservation = false;
        boolean keywordExisted = false;


        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();

        if (types != null) {
            for (String filterString : types) {
                if (filterString.equalsIgnoreCase("student")) student = true;
                if (filterString.equalsIgnoreCase("trainer")) trainer = true;
            }
        }

        if (categories != null) {
            for (String viewByString : categories) {
                if (viewByString.equalsIgnoreCase("inform")) inform = true;
                if (viewByString.equalsIgnoreCase("remind")) remind = true;
                if (viewByString.equalsIgnoreCase("score")) score = true;
                if (viewByString.equalsIgnoreCase("reservation")) reservation = true;
            }
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        if (!keyword.isBlank() && (student || trainer) && (inform || remind || score || reservation)) {
            keywordExisted = true;
            List<EmailTemplate> list = emailTemplateRepository.findByNameAndTypeAndCategory(pageable, keyword, keywordExisted, student, trainer, inform, remind, score, reservation);
            List<EmailTemplateDto2> list2 = mapToDto(list);
            return getPage(list2, pageNumber, pageSize, sort);
        } else if ((student || trainer) && (inform || remind || score || reservation) && keyword.isBlank()) {
            List<EmailTemplate> list = emailTemplateRepository.findTypeAndCategory(pageable, keyword, keywordExisted, student, trainer, inform, remind, score, reservation);
            List<EmailTemplateDto2> list2 = mapToDto(list);
            return getPage(list2, pageNumber, pageSize, sort);
        } else if (!keyword.isBlank() && !(student || trainer) && !(inform || remind || score || reservation)) {
            keywordExisted = true;
            List<EmailTemplate> list = emailTemplateRepository.findByName(pageable, keyword, keywordExisted);
            List<EmailTemplateDto2> list2 = mapToDto(list);
            return getPage(list2, pageNumber, pageSize, sort);
        } else if ((student || trainer) && !(inform && remind && score && reservation) && keyword.isBlank()) {
            List<EmailTemplate> list = emailTemplateRepository.findTypeOrCategory1(pageable, student, trainer);
            List<EmailTemplateDto2> list2 = mapToDto(list);
            return getPage(list2, pageNumber, pageSize, sort);
        } else if ((student || trainer) && !(inform && remind && score && reservation) && !keyword.isBlank()) {
            keywordExisted = true;
            List<EmailTemplate> list = emailTemplateRepository.findTypeOrCategory2(pageable, student, trainer, keyword, keywordExisted);
            List<EmailTemplateDto2> list2 = mapToDto(list);
            return getPage(list2, pageNumber, pageSize, sort);
        } else if (!(student && trainer) && (inform || remind || score || reservation) && keyword.isBlank()) {
            List<EmailTemplate> list = emailTemplateRepository.findTypeOrCategory3(pageable, inform, remind, score, reservation);
            List<EmailTemplateDto2> list2 = mapToDto(list);
            return getPage(list2, pageNumber, pageSize, sort);
        } else if (!(student && trainer) && (inform || remind || score || reservation) && !keyword.isBlank()) {
            keywordExisted = true;
            List<EmailTemplate> list = emailTemplateRepository.findTypeOrCategory4(pageable, keyword, keywordExisted, inform, remind, score, reservation);
            List<EmailTemplateDto2> list2 = mapToDto(list);
            return getPage(list2, pageNumber, pageSize, sort);
        }
        List<EmailTemplate> list = emailTemplateRepository.findAllEmailTemplate(pageable);
        List<EmailTemplateDto2> list2 = mapToDto(list);
        return getPage(list2, pageNumber, pageSize, sort);
    }

    private Page<EmailTemplateDto2> getPage(List<EmailTemplateDto2> resultList, int pageNumber, int pageSize, Sort sort) {
        int start = Math.min((pageNumber - 1) * pageSize, resultList.size());
        int end = Math.min(start + pageSize, resultList.size());
        return new PageImpl<>(resultList.subList(start, end), PageRequest.of(pageNumber - 1, pageSize, sort), resultList.size());
    }


    @Override
    public List<EmailHistoryDto> getEmailSentToStudent(String idStudent) {

        List<EmailSend> emailSends = emailSendRepository.getEmailSentToStudentLog(idStudent);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ModelMapper modelMapper = new ModelMapper();
        List<EmailHistoryDto> emailHistoryDtos = new ArrayList<>();

        for (EmailSend emailSend : emailSends) {
            EmailHistoryDto emailHistoryDto = modelMapper.map(emailSend, EmailHistoryDto.class);
            emailHistoryDto.setModifiedBy(emailSend.getUser().getFullName());
            emailHistoryDto.setSendDate(formatter.format(emailSend.getSendDate()));
            emailHistoryDto.setAction(emailSend.getEmailTemplate().getName() + " - " + emailSend.getEmailTemplate().getCategory());
            emailHistoryDtos.add(emailHistoryDto);
        }

        return emailHistoryDtos.stream()
                .sorted(Comparator.comparing(EmailHistoryDto::getSendDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void sendEmailToTrainer(HttpServletRequest request, String idClass, Long idTemplate) {

        ClassEntity clazz = classRepository.findById(idClass).orElseThrow(() -> new RuntimeException("Class Not Found!"));
        EmailTemplate template = emailTemplateRepository.findById(idTemplate).orElseThrow(() -> new RuntimeException("Template Not Found!"));
        UserEntity user = getUserInfo(request);
        String content = null;

        if (template.getUseDear()) {
            content = "<p>Dear " + clazz.getCreatedBy().getFullName() + "</p> " +
                    template.getContent() + "<p>" + user.getFullName() + "</p>";
            sendEmailWithHtmlTemplate(clazz.getCreatedBy().getEmail(), template.getName(), content);
        } else {
            content = template.getContent() + "<p>" + user.getFullName() + "</p>";
            sendEmailWithHtmlTemplate(clazz.getCreatedBy().getEmail(), template.getName(), content);
        }
    }

    @Override
    public ReservationEmailPreviewDTO previewMail(HttpServletRequest request, Long idTemplate, String idClass) {
        ClassEntity clazz = classRepository.findById(idClass).orElseThrow(() -> new RuntimeException("Class Not Found!"));
        EmailTemplate template = emailTemplateRepository.findById(idTemplate).orElseThrow(() -> new RuntimeException("Template Not Found!"));
        UserEntity user = getUserInfo(request);


        if (template.getUseDear()) {
            return preview(template, template.getName(), clazz.getCreatedBy().getEmail(), "Dear " + clazz.getCreatedBy().getFullName() + " " + template.getContent() + " " + user.getFullName());

        } else {
            return preview(template, template.getName(), clazz.getCreatedBy().getEmail(), template.getContent() + " " + user.getFullName());
        }

    }

}
