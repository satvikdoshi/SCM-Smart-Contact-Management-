# SCM - Smart Contact Management

SCM (Smart Contact Management) is a web-based application that allows users to manage their contacts efficiently. It provides functionalities like Google and GitHub authentication, contact management, profile editing, pagination, exporting contacts to Excel, theming options.

## Features

- **User Authentication**: Signup and login with Google and GitHub OAuth.
- **Contact Management**: Add, edit, delete, and manage contacts.
- **Profile Editing**: Edit user profile details.
- **Export Contacts**: Export contacts to Excel.
- **Theming**: Switch between different themes (Light/Dark).
- **Feedback**: Send feedback directly from the application.

## Technologies Used

- **Backend**: Spring Boot, Java
- **Frontend**: Thymeleaf, HTML, CSS, Tailwind CSS
- **Database**: MySQL
- **Cloud**: Cloudinary (for profile storage)
- **Other**: OAuth (Google, GitHub)

## Setup Instructions

### Clone the Repository
git clone https://github.com/satvikdoshi/SCM-Smart-Contact-Management-.git
cd SCM-Smart-Contact-Management


# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/scm_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update

# AWS Configuration (Can use any cloud service)
cloud.aws.credentials.access-key=YOUR_AWS_ACCESS_KEY
cloud.aws.credentials.secret-key=YOUR_AWS_SECRET_KEY
cloud.aws.region.static=YOUR_AWS_REGION
cloud.aws.s3.bucket=YOUR_S3_BUCKET_NAME

# Mail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_EMAIL_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# OAuth2 Configuration (Google & GitHub)
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET

# Application Configuration
app.email.token.expiration=3600000
app.theme.default=light
