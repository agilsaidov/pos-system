package com.app.pos.system.email;

import com.app.pos.system.exception.MailException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendOtpWithAttachment(String to, String otp) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject("POS System - OTP Verification");
            mimeMessageHelper.setText(getBody(otp), true);

            javaMailSender.send(mimeMessage);

        }catch (Exception e){
            throw new MailException("MAIL_SEND_FAILED",e.getMessage());
        }
    }

    private String getBody(String otp) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
            </head>
            <body style="margin:0; padding:0; background-color:#f0f2f5; font-family:'Segoe UI', Arial, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="padding: 40px 20px;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background:#ffffff; border-radius:12px; overflow:hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.08);">
                                
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #1a1a2e 0%%, #16213e 50%%, #0f3460 100%%); padding: 40px; text-align:center;">
                                        <h1 style="margin:0; color:#ffffff; font-size:26px; font-weight:700; letter-spacing:2px;">POS SYSTEM</h1>
                                        <p style="margin:8px 0 0; color:#a0b4c8; font-size:13px; letter-spacing:1px;">SECURITY VERIFICATION</p>
                                    </td>
                                </tr>

                                <!-- Body -->
                                <tr>
                                    <td style="padding: 50px 40px;">
                                        <p style="margin:0 0 16px; color:#444; font-size:15px;">Hello,</p>
                                        <p style="margin:0 0 32px; color:#666; font-size:15px; line-height:1.6;">
                                            We received a request to reset your password. Use the verification code below to proceed.
                                        </p>

                                        <!-- OTP Box -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:32px;">
                                            <tr>
                                                <td align="center">
                                                    <div style="display:inline-block; background:#f8f9ff; border:2px dashed #0f3460; border-radius:12px; padding:24px 48px;">
                                                        <p style="margin:0 0 8px; color:#888; font-size:12px; letter-spacing:2px; text-transform:uppercase;">Your OTP Code</p>
                                                        <p style="margin:0; color:#0f3460; font-size:42px; font-weight:800; letter-spacing:12px;">%s</p>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>

                                        <!-- Timer warning -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:32px;">
                                            <tr>
                                                <td style="background:#fff8e1; border-left:4px solid #ffc107; border-radius:4px; padding:12px 16px;">
                                                    <p style="margin:0; color:#856404; font-size:13px;">
                                                        ⏳ This code expires in <strong>3 minutes</strong>. Do not share it with anyone.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>

                                        <p style="margin:0; color:#999; font-size:13px; line-height:1.6;">
                                            If you did not request a password reset, please ignore this email or contact your administrator immediately.
                                        </p>
                                    </td>
                                </tr>

                                <!-- Footer -->
                                <tr>
                                    <td style="background:#f8f9fa; padding:24px 40px; border-top:1px solid #eee; text-align:center;">
                                        <p style="margin:0; color:#aaa; font-size:12px;">
                                            © 2026 POS System · This is an automated message, please do not reply.
                                        </p>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """, otp);
    }
}
