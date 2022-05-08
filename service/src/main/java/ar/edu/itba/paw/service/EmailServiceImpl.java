package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    Environment env;

    @Async
    @Override
    public void sendEmailSelf(String subject, String body) {
        sendEmail(env.getProperty("spring.mail.username"), subject, body);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@argu.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try {
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Override
    public void notifyNewPost(String to, String from, long debateId, String debateName) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlMsg = "<!DOCTYPE html\n" +
                "    PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" +
                "    style=\"width: 100%; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; padding: 0; Margin: 0;\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\n" +
                "    <meta content=\"telephone=no\" name=\"format-detection\">\n" +
                "    <meta name=\"color-scheme\" content=\"only\">\n" +
                "    <title></title>\n" +
                "    <!--[if (mso 16)]>\n" +
                "    <style type=\"text/css\">\n" +
                "    a {text-decoration: none;}\n" +
                "    </style>\n" +
                "    <![endif]-->\n" +
                "    <!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]-->\n" +
                "    <!--[if gte mso 9]>\n" +
                "<xml>\n" +
                "    <o:OfficeDocumentSettings>\n" +
                "    <o:AllowPNG></o:AllowPNG>\n" +
                "    <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "    </o:OfficeDocumentSettings>\n" +
                "</xml>\n" +
                "<![endif]-->\n" +
                "    <style>\n" +
                "        @media only screen and (max-width: 600px) {\n" +
                "\n" +
                "            p,\n" +
                "            ul li,\n" +
                "            ol li,\n" +
                "            a {\n" +
                "                font-size: 16px !important;\n" +
                "                line-height: 150% !important;\n" +
                "            }\n" +
                "\n" +
                "            h1 {\n" +
                "                font-size: 30px !important;\n" +
                "                text-align: center;\n" +
                "                line-height: 120% !important;\n" +
                "            }\n" +
                "\n" +
                "            h2 {\n" +
                "                font-size: 26px !important;\n" +
                "                text-align: center;\n" +
                "                line-height: 120% !important;\n" +
                "            }\n" +
                "\n" +
                "            h3 {\n" +
                "                font-size: 20px !important;\n" +
                "                text-align: center;\n" +
                "                line-height: 120% !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-menu td a {\n" +
                "                font-size: 16px !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-header-body p,\n" +
                "            .es-header-body ul li,\n" +
                "            .es-header-body ol li,\n" +
                "            .es-header-body a {\n" +
                "                font-size: 16px !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-footer-body p,\n" +
                "            .es-footer-body ul li,\n" +
                "            .es-footer-body ol li,\n" +
                "            .es-footer-body a {\n" +
                "                font-size: 16px !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-infoblock p,\n" +
                "            .es-infoblock ul li,\n" +
                "            .es-infoblock ol li,\n" +
                "            .es-infoblock a {\n" +
                "                font-size: 12px !important;\n" +
                "            }\n" +
                "\n" +
                "            *[class=\"gmail-fix\"] {\n" +
                "                display: none !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-m-txt-c {\n" +
                "                text-align: center !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-m-txt-r {\n" +
                "                text-align: right !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-m-txt-l {\n" +
                "                text-align: left !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-m-txt-r img,\n" +
                "            .es-m-txt-c img,\n" +
                "            .es-m-txt-l img {\n" +
                "                display: inline !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-button-border {\n" +
                "                display: block !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-btn-fw {\n" +
                "                border-width: 10px 0px !important;\n" +
                "                text-align: center !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-adaptive table,\n" +
                "            .es-btn-fw,\n" +
                "            .es-btn-fw-brdr,\n" +
                "            .es-left,\n" +
                "            .es-right {\n" +
                "                width: 100% !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-content table,\n" +
                "            .es-header table,\n" +
                "            .es-footer table,\n" +
                "            .es-content,\n" +
                "            .es-footer,\n" +
                "            .es-header {\n" +
                "                width: 100% !important;\n" +
                "                max-width: 600px !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-adapt-td {\n" +
                "                display: block !important;\n" +
                "                width: 100% !important;\n" +
                "            }\n" +
                "\n" +
                "            .adapt-img {\n" +
                "                width: 100% !important;\n" +
                "                height: auto !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-m-p0 {\n" +
                "                padding: 0px !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-m-p0r {\n" +
                "                padding-right: 0px !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-m-p0l {\n" +
                "                padding-left: 0px !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-m-p0t {\n" +
                "                padding-top: 0px !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-m-p0b {\n" +
                "                padding-bottom: 0 !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-m-p20b {\n" +
                "                padding-bottom: 20px !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-mobile-hidden,\n" +
                "            .es-hidden {\n" +
                "                display: none !important;\n" +
                "            }\n" +
                "\n" +
                "            tr.es-desk-hidden,\n" +
                "            td.es-desk-hidden,\n" +
                "            table.es-desk-hidden {\n" +
                "                width: auto !important;\n" +
                "                overflow: visible !important;\n" +
                "                float: none !important;\n" +
                "                max-height: inherit !important;\n" +
                "                line-height: inherit !important;\n" +
                "            }\n" +
                "\n" +
                "            tr.es-desk-hidden {\n" +
                "                display: table-row !important;\n" +
                "            }\n" +
                "\n" +
                "            table.es-desk-hidden {\n" +
                "                display: table !important;\n" +
                "            }\n" +
                "\n" +
                "            td.es-desk-menu-hidden {\n" +
                "                display: table-cell !important;\n" +
                "            }\n" +
                "\n" +
                "            .es-menu td {\n" +
                "                width: 1% !important;\n" +
                "            }\n" +
                "\n" +
                "            table.es-table-not-adapt,\n" +
                "            .esd-block-html table {\n" +
                "                width: auto !important;\n" +
                "            }\n" +
                "\n" +
                "            table.es-social td {\n" +
                "                display: inline-block !important;\n" +
                "            }\n" +
                "\n" +
                "            table.es-social {\n" +
                "                display: inline-block !important;\n" +
                "            }\n" +
                "\n" +
                "            a.es-button,\n" +
                "            button.es-button {\n" +
                "                font-size: 20px !important;\n" +
                "                display: block !important;\n" +
                "                border-width: 10px 0px 10px 0px !important;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body\n" +
                "    style=\"width: 100%; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; padding: 0; Margin: 0;\">\n" +
                "    <div class=\"es-wrapper-color\" style=\"background-color: #cccccc;\">\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\"\n" +
                "            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; table-layout: fixed; width: 100%; background-color: transparent; background-image: ;background-repeat: repeat; background-position: center top;\"\n" +
                "            width=\"100%\" bgcolor=\"transparent\" background=\";background-repeat: repeat\">\n" +
                "            <tbody>\n" +
                "                <tr style=\"border-collapse: collapse;\">\n" +
                "                    <td class=\"esd-stripe\" esd-custom-block-id=\"3089\" align=\"center\" style=\"padding: 0; Margin: 0;\">\n" +
                "                        <table class=\"es-header-body\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"\n" +
                "                            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; background-color: #212D40;\"\n" +
                "                            bgcolor=\"#212D40\">\n" +
                "                            <tbody>\n" +
                "                                <tr style=\"border-collapse: collapse;\">\n" +
                "                                    <td class=\"esd-structure es-p10t es-p10b es-p10r es-p10l\" align=\"left\"\n" +
                "                                        style=\"padding: 0; Margin: 0; padding-top: 10px; padding-bottom: 10px; padding-left: 10px; padding-right: 10px;\">\n" +
                "                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"\n" +
                "                                            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px;\">\n" +
                "                                            <tbody>\n" +
                "                                                <tr style=\"border-collapse: collapse;\">\n" +
                "                                                    <td class=\"esd-container-frame\" width=\"580\" valign=\"top\"\n" +
                "                                                        align=\"center\" style=\"padding: 0; Margin: 0;\">\n" +
                "                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"\n" +
                "                                                            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px;\">\n" +
                "                                                            <tbody>\n" +
                "                                                                <tr style=\"border-collapse: collapse;\">\n" +
                "                                                                    <td class=\"esd-block-image\" align=\"center\"\n" +
                "                                                                        style=\"padding: 0; Margin: 0; font-size: 0;\"><a\n" +
                "                                                                            href=\"pawserver.it.itba.edu.ar/paw-2022a-06\"\n" +
                "                                                                            target=\"_blank\"\n" +
                "                                                                            style=\"-webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; text-decoration: underline; font-size: 14px; color: #cccccc;\"><img\n" +
                "                                                                                src=\"https://i.imgur.com/BTUoUee.png\"\n" +
                "                                                                                alt=\"ARGU Logo\" title=\"ARGU Logo\"\n" +
                "                                                                                width=\"250\"\n" +
                "                                                                                style=\"display: block; border: 0; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic;\"></a>\n" +
                "                                                                    </td>\n" +
                "                                                                </tr>\n" +
                "                                                            </tbody>\n" +
                "                                                        </table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "        <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"\n" +
                "            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; table-layout: fixed; width: 100%;\"\n" +
                "            width=\"100%\">\n" +
                "            <tbody>\n" +
                "                <tr style=\"border-collapse: collapse;\">\n" +
                "                    <td class=\"esd-stripe\" esd-custom-block-id=\"3109\" align=\"center\" style=\"padding: 0; Margin: 0;\">\n" +
                "                        <table class=\"es-content-body\"\n" +
                "                            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; background-color: #D66853;\"\n" +
                "                            width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#D66853\" align=\"center\">\n" +
                "                            <tbody>\n" +
                "                                <tr style=\"border-collapse: collapse;\">\n" +
                "                                    <td class=\"esd-structure es-p20t es-p20b es-p40r es-p40l\"\n" +
                "                                        esd-general-paddings-checked=\"true\" align=\"left\"\n" +
                "                                        style=\"padding: 0; Margin: 0; padding-top: 20px; padding-bottom: 20px; padding-left: 40px; padding-right: 40px;\">\n" +
                "                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"\n" +
                "                                            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px;\">\n" +
                "                                            <tbody>\n" +
                "                                                <tr style=\"border-collapse: collapse;\">\n" +
                "                                                    <td class=\"esd-container-frame\" width=\"520\" valign=\"top\"\n" +
                "                                                        align=\"center\" style=\"padding: 0; Margin: 0;\">\n" +
                "                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"\n" +
                "                                                            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px;\">\n" +
                "                                                            <tbody>\n" +
                "                                                                <tr style=\"border-collapse: collapse;\">\n" +
                "                                                                    <td class=\"esd-block-text es-p10b\" align=\"left\"\n" +
                "                                                                        style=\"padding: 0; Margin: 0; padding-bottom: 10px;\">\n" +
                "                                                                        <p\n" +
                "                                                                            style=\"Margin: 0; -webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-size: 14px; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; line-height: 150%; color: #ffff;\">\n" +
                "                                                                            <span\n" +
                "                                                                                style=\"font-size: 16px; line-height: 150%;\">Hi!</span>\n" +
                "                                                                        </p>\n" +
                "                                                                    </td>\n" +
                "                                                                </tr>\n" +
                "                                                                <tr style=\"border-collapse: collapse;\">\n" +
                "                                                                    <td class=\"esd-block-text\" align=\"left\"\n" +
                "                                                                        style=\"padding: 0; Margin: 0;\">\n" +
                "                                                                        <p\n" +
                "                                                                            style=\"Margin: 0; -webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-size: 14px; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; line-height: 150%; color: #ffff;\">\n" +
                "                                                                            We are reaching out to you to let you know " +
                "                                                                            that " + from + " just posted a new " +
                "                                                                            argument in the debate " +
                "                                                                            <a href=\"pawserver.it.itba.edu.ar/paw-2022a-06/debates/" + debateId +
                "                                                                            \">" + debateName + "</a>" +
                "                                                                            that you are subscribed to.\n" +
                "                                                                        </p>\n" +
                "                                                                    </td>\n" +
                "                                                                </tr>\n" +
                "                                                            </tbody>\n" +
                "                                                        </table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-footer\" align=\"center\"\n" +
                "            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; table-layout: fixed; width: 100%; background-color: transparent; background-image: ;background-repeat: repeat; background-position: center top;\"\n" +
                "            width=\"100%\" bgcolor=\"transparent\" background=\";background-repeat: repeat\">\n" +
                "            <tbody>\n" +
                "                <tr style=\"border-collapse: collapse;\">\n" +
                "                    <td class=\"esd-stripe\" esd-custom-block-id=\"3104\" align=\"center\" style=\"padding: 0; Margin: 0;\">\n" +
                "                        <table class=\"es-footer-body\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"\n" +
                "                            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; background-color: #364156;\"\n" +
                "                            bgcolor=\"#364156\">\n" +
                "                            <tbody>\n" +
                "                                <tr style=\"border-collapse: collapse;\">\n" +
                "                                    <td class=\"esd-structure es-p15b es-p20r es-p20l\"\n" +
                "                                        esd-general-paddings-checked=\"false\" align=\"left\"\n" +
                "                                        style=\"padding: 0; Margin: 0; padding-bottom: 15px; padding-left: 20px; padding-right: 20px;\">\n" +
                "                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"\n" +
                "                                            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px;\">\n" +
                "                                            <tbody>\n" +
                "                                                <tr style=\"border-collapse: collapse;\">\n" +
                "                                                    <td class=\"esd-container-frame\" width=\"560\" valign=\"top\"\n" +
                "                                                        align=\"center\" style=\"padding: 0; Margin: 0;\">\n" +
                "                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"\n" +
                "                                                            style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px;\">\n" +
                "                                                            <tbody>\n" +
                "                                                                <!--PONER NOMBRE DEL EVENTO AL QUE SE ANOTARON-->\n" +
                "                                                                <tr style=\"border-collapse: collapse;\">\n" +
                "                                                                    <td esdev-links-color=\"#333333\" align=\"left\"\n" +
                "                                                                        class=\"esd-block-text\"\n" +
                "                                                                        style=\"padding: 0; Margin: 0;\">\n" +
                "                                                                        <p\n" +
                "                                                                            style=\"Margin: 0; -webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; color: #efefef; font-size: 12px; line-height: 150%;\">\n" +
                "                                                                            ©\n" +
                "                                                                            2022 ARGU, All rights reserved</p>\n" +
                "                                                                    </td>\n" +
                "                                                                </tr>\n" +
                "                                                            </tbody>\n" +
                "                                                        </table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "    </div>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
        
        try {
            helper.setText(htmlMsg, true); // Use this or above line.
            helper.setTo(to);
            helper.setSubject("New post in a debate you're following!");
            helper.setFrom("noreply@argu.com"); //TODO: Actualizar el nombre
            emailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace(); //TODO: handle exception
        }
    }

    public static class ResourceReader {
        public static String asString(Resource resource) {
            try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                return FileCopyUtils.copyToString(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
