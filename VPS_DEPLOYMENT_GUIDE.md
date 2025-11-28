# 🚀 VPS Deployment Guide

## 📋 Complete Guide to Deploy Personal Finance Manager on Ubuntu VPS

This guide will help you deploy your Spring Boot application to an Ubuntu VPS with MySQL, Nginx, SSL, and automatic startup.

---

## 🛠️ Prerequisites

- Ubuntu VPS (18.04+ recommended)
- SSH access with sudo privileges
- Domain name (optional, but recommended for SSL)
- Your Gemini API key

---

## 🎯 Quick Start (TL;DR)

```bash
# 1. SSH into your VPS
ssh your-user@your-vps-ip

# 2. Update system
sudo apt update && sudo apt upgrade -y

# 3. Install required packages
sudo apt install openjdk-11-jdk maven mysql-server nginx certbot python3-certbot-nginx -y

# 4. Setup MySQL (see detailed steps below)
# 5. Deploy application (see detailed steps below)
# 6. Configure Nginx (see detailed steps below)
# 7. Setup SSL (see detailed steps below)
```

---

## 📚 Detailed Step-by-Step Guide

### 1️⃣ Server Setup & Updates

```bash
# Update package lists and upgrade packages
sudo apt update && sudo apt upgrade -y

# Install essential tools
sudo apt install curl wget unzip git htop -y

# Set timezone to Vietnam
sudo timedatectl set-timezone Asia/Ho_Chi_Minh
```

### 2️⃣ Install Java Development Kit

```bash
# Install OpenJDK 11 (compatible with Spring Boot 2.1.3)
sudo apt install openjdk-11-jdk -y

# Verify Java installation
java -version
javac -version

# Set JAVA_HOME (add to ~/.bashrc)
echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64' >> ~/.bashrc
source ~/.bashrc
```

### 3️⃣ Install Apache Maven

```bash
# Install Maven
sudo apt install maven -y

# Verify Maven installation
mvn -version
```

### 4️⃣ MySQL Database Setup

```bash
# Install MySQL Server
sudo apt install mysql-server -y

# Secure MySQL installation
sudo mysql_secure_installation

# Login to MySQL
sudo mysql -u root -p

# Create database and user
```

**MySQL Commands:**
```sql
-- Create database
CREATE DATABASE personal_finance_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create dedicated user (recommended for production)
CREATE USER 'financeapp'@'localhost' IDENTIFIED BY 'StrongPassword123!';

-- Grant privileges
GRANT ALL PRIVILEGES ON personal_finance_db.* TO 'financeapp'@'localhost';

-- Or use root user (simpler for now)
-- Your app is configured to use root with password: minhkhuat123

FLUSH PRIVILEGES;
EXIT;
```

### 5️⃣ Deploy Your Application

#### Method A: Upload & Build on VPS (Recommended)

```bash
# Create application directory
sudo mkdir -p /opt/finance-app
sudo chown $USER:$USER /opt/finance-app
cd /opt/finance-app

# Clone your repository or upload files
git clone <your-repo-url> .
# OR upload your project files using scp/sftp

# Build the application
mvn clean package -DskipTests

# Verify JAR file is created
ls -la target/personal-finance-manager-*.jar
```

#### Method B: Upload Pre-built JAR

```bash
# Build locally first
mvn clean package -DskipTests

# Upload to VPS
scp target/personal-finance-manager-1.0.0.jar your-user@your-vps-ip:/opt/finance-app/

# SSH into VPS and continue
ssh your-user@your-vps-ip
cd /opt/finance-app
```

### 6️⃣ Configure Environment Variables

```bash
# Set Gemini API key
export GEMINI_API_KEY=your_actual_gemini_api_key

# Add to .bashrc for persistence
echo 'export GEMINI_API_KEY=your_actual_gemini_api_key' >> ~/.bashrc
source ~/.bashrc

# Verify it's set
echo $GEMINI_API_KEY
```

### 7️⃣ Test Application Manually

```bash
cd /opt/finance-app

# Run the application to test
java -jar target/personal-finance-manager-1.0.0.jar

# Test in another terminal:
curl http://localhost:8080

# If it works, stop with Ctrl+C and continue
```

### 8️⃣ Create Systemd Service (Auto-start)

```bash
# Create systemd service file
sudo nano /etc/systemd/system/finance-app.service
```

**Service Configuration:**
```ini
[Unit]
Description=Personal Finance Manager Application
After=network.target mysql.service
Wants=mysql.service

[Service]
Type=simple
User=your-username
Group=your-username
WorkingDirectory=/opt/finance-app
ExecStart=/usr/bin/java -jar /opt/finance-app/target/personal-finance-manager-1.0.0.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=finance-app

# Environment Variables
Environment="GEMINI_API_KEY=your_actual_gemini_api_key"

[Install]
WantedBy=multi-user.target
```

```bash
# Enable and start the service
sudo systemctl daemon-reload
sudo systemctl enable finance-app
sudo systemctl start finance-app

# Check status
sudo systemctl status finance-app

# Check logs
sudo journalctl -u finance-app -f
```

### 9️⃣ Configure Firewall

```bash
# Enable UFW firewall
sudo ufw enable

# Allow SSH (already allowed)
sudo ufw allow ssh

# Allow HTTP and HTTPS
sudo ufw allow 80
sudo ufw allow 443

# Check firewall status
sudo ufw status
```

### 1️⃣0️⃣ Install & Configure Nginx (Reverse Proxy)

```bash
# Install Nginx
sudo apt install nginx -y

# Start and enable Nginx
sudo systemctl start nginx
sudo systemctl enable nginx

# Create Nginx configuration for your app
sudo nano /etc/nginx/sites-available/finance-app
```

**Nginx Configuration:**
```nginx
server {
    listen 80;
    server_name your-domain.com your-vps-ip;

    # Redirect HTTP to HTTPS (uncomment after SSL setup)
    # return 301 https://$server_name$request_uri;

    location / {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;

        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # Increase client body size for file uploads
    client_max_body_size 10M;
}
```

```bash
# Enable the site
sudo ln -s /etc/nginx/sites-available/finance-app /etc/nginx/sites-enabled/

# Remove default site
sudo rm /etc/nginx/sites-enabled/default

# Test Nginx configuration
sudo nginx -t

# Restart Nginx
sudo systemctl restart nginx
```

### 1️⃣1️⃣ Setup SSL Certificate (Let's Encrypt)

#### If you have a domain name:

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx -y

# Get SSL certificate
sudo certbot --nginx -d your-domain.com

# Follow the prompts to setup SSL
# Choose redirect option to force HTTPS
```

#### If you don't have a domain (use self-signed):

```bash
# Create SSL directory
sudo mkdir -p /etc/nginx/ssl

# Generate self-signed certificate
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout /etc/nginx/ssl/nginx.key \
    -out /etc/nginx/ssl/nginx.crt \
    -subj "/C=VN/ST=HoChiMinh/L=HoChiMinh/O=Finance App/CN=your-vps-ip"

# Update Nginx config for HTTPS
sudo nano /etc/nginx/sites-available/finance-app
```

**HTTPS Nginx Configuration:**
```nginx
server {
    listen 443 ssl;
    server_name your-vps-ip;

    ssl_certificate /etc/nginx/ssl/nginx.crt;
    ssl_certificate_key /etc/nginx/ssl/nginx.key;

    # SSL settings
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    location / {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
    }

    client_max_body_size 10M;
}

server {
    listen 80;
    server_name your-vps-ip;
    return 301 https://$server_name$request_uri;
}
```

### 1️⃣2️⃣ Final Testing

```bash
# Test the application
curl -k https://your-vps-ip

# Check service status
sudo systemctl status finance-app
sudo systemctl status nginx

# Check logs if needed
sudo journalctl -u finance-app -f
sudo tail -f /var/log/nginx/access.log
```

---

## 🔧 Advanced Configuration

### Database Backup Script

```bash
# Create backup script
sudo nano /usr/local/bin/backup-finance-db.sh
```

```bash
#!/bin/bash
# Database backup script
BACKUP_DIR="/opt/backups"
DATE=$(date +%Y%m%d_%H%M%S)
DB_NAME="personal_finance_db"
DB_USER="root"

# Create backup directory
mkdir -p $BACKUP_DIR

# Create backup
mysqldump -u $DB_USER -p$DB_PASSWORD $DB_NAME > $BACKUP_DIR/backup_$DATE.sql

# Keep only last 7 days of backups
find $BACKUP_DIR -name "backup_*.sql" -mtime +7 -delete

echo "Backup completed: $BACKUP_DIR/backup_$DATE.sql"
```

```bash
# Make script executable
sudo chmod +x /usr/local/bin/backup-finance-db.sh

# Setup daily backup (3 AM)
sudo crontab -e
# Add this line:
0 3 * * * /usr/local/bin/backup-finance-db.sh
```

### Monitoring Script

```bash
# Create monitoring script
sudo nano /usr/local/bin/monitor-finance-app.sh
```

```bash
#!/bin/bash
# Monitor application and restart if needed
SERVICE_NAME="finance-app"

if ! systemctl is-active --quiet $SERVICE_NAME; then
    echo "$SERVICE_NAME is down, restarting..."
    systemctl restart $SERVICE_NAME
    echo "$(date): $SERVICE_NAME restarted" >> /var/log/finance-app-monitor.log
fi
```

```bash
# Make executable and setup monitoring
sudo chmod +x /usr/local/bin/monitor-finance-app.sh
# Add to crontab every 5 minutes:
*/5 * * * * /usr/local/bin/monitor-finance-app.sh
```

---

## 🚨 Troubleshooting

### Application Won't Start:
```bash
# Check service status
sudo systemctl status finance-app

# Check logs
sudo journalctl -u finance-app -n 50

# Check Java version
java -version

# Check port availability
sudo netstat -tlnp | grep :8080
```

### Database Connection Issues:
```bash
# Test MySQL connection
mysql -u root -p -e "SHOW DATABASES;"

# Check MySQL status
sudo systemctl status mysql

# Reset MySQL password if needed
sudo mysql_secure_installation
```

### Nginx Issues:
```bash
# Test Nginx config
sudo nginx -t

# Check Nginx logs
sudo tail -f /var/log/nginx/error.log

# Restart Nginx
sudo systemctl restart nginx
```

### Port Already in Use:
```bash
# Find what's using port 8080
sudo lsof -i :8080

# Kill the process if needed
sudo kill -9 PID
```

---

## 🎉 Your Application is Now Live!

Your Personal Finance Manager should now be accessible at:
- **HTTP**: `http://your-vps-ip`
- **HTTPS**: `https://your-vps-ip` (or `https://your-domain.com`)

### Features Available:
- ✅ User registration and login
- ✅ Transaction management
- ✅ Budget tracking
- ✅ Goal setting
- ✅ **AI Chat Assistant** (with API key configured)
- ✅ Responsive design
- ✅ Vietnamese language support

### Next Steps:
1. **Test all features** thoroughly
2. **Set up regular backups**
3. **Monitor application performance**
4. **Update default admin password**
5. **Consider adding monitoring/alerting**

---

## 📞 Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review application logs
3. Verify all configurations
4. Test each component individually

Your application is now securely deployed with SSL, automatic startup, and proper monitoring! 🚀