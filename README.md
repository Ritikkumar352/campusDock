# campusDock initialized
# 🏫 Campus Dock

Campus Dock is a modern college-centric platform that digitizes the entire campus experience, offering modules like canteen ordering, cart and payment system, admin dashboards, and anonymous social features like confessions — all role-based and fully containerized, deployed on AWS.

## 📚 Table of Contents

* [🔍 Overview](#-overview)
* [🧹 Features](#-features)
* [🛠️ Tech Stack](#-tech-stack)
* [🧱 System Architecture](#-system-architecture)
* [🌐 Deployment Architecture](#-deployment-architecture)
* [🔐 Security & Best Practices](#-security--best-practices)
* [🎺 Database Design](#-database-design)
* [🧪 Testing](#-testing)
* [🧰 Future Improvements](#-future-improvements)
* [📸 Screenshots / GIFs](#-screenshots--gifs)
* [📌 License & Author](#-license--author)

## 🔍 Overview

Campus Dock is designed to digitize real-life college experiences. It centralizes:

* 🎓 Campus selection and user authentication (students, admins, canteen managers)
* 🍔 Smart canteen system with menu, media, order, and payment
* 📦 Real-time cart tracking
* 📢 A social confession module with anonymous users and user tagging
* 📋 Role-based dashboards for super admins and canteen admins

## 🧹 Features

| Module            | Description                                                              |
| ----------------- | ------------------------------------------------------------------------ |
| 🏫 College Module | Add/view colleges with domain linking                                    |
| 👤 User System    | role-based access (student, admin, super admin)      |
| 🍽️ Canteen       | Add/manage canteens and real-time open/close tracking                    |
| 📟 Menu Items     | Upload menu with media support (images/videos)                           |
| 🛒 Cart & Orders  | Add to cart, order placement, and history                                |
| 💸 Payments       | Pluggable payment gateway integration (currently PhonePe)                |
| 📣 Social Feed    | Anonymous confessions with user tagging and acceptance system            |
| 🛡️ Admin Panel   | Central dashboard for managing colleges, users, canteens, and menu items |

## 🛠️ Tech Stack

| Layer               | Tech                                             |
| ------------------- | ------------------------------------------------ |
| 🎯 Frontend         | React (JSX), Tailwind, Shadcn UI                 |
| 🔧 Backend          | Spring Boot, Spring Security, REST APIs          |
| 💾 Database         | PostgreSQL (via Docker), JPA/Hibernate           |
| 📤 Media Uploads    | AWS S3         |
| 🛆 Containerization | Docker + Docker Compose                          |
| ✨ Hosting           | AWS EC2 + HTTPS via Nginx + Certbot + Cloudflare |

## 🧱 System Architecture

* Microservice-like modular structure
* Role-based access at the API and UI level
* Modular DTOs and clean controller/service/repository layering
* Separate media, order, payment, College, Canteen and user modules

## 🌐 Deployment Architecture

* 🌐 **Domain Management**: Cloudflare DNS with HTTPS enforced
* ⛓️ **Nginx**: Reverse proxy with SSL termination
* 🏠 **EC2**: Ubuntu-based virtual machine for backend hosting
* 📆 **Docker Compose**: Orchestrating all Spring Boot containers
* 🚪 **Port Exposed**: Only required backend ports (e.g., 8080 internal)
* 🔒 **HTTPS**: Configured using Certbot + Nginx

## 🔐 Security & Best Practices

* OEmail login (only using college specific email)
* Role-based authorization for admin-level APIs
* 
* Media uploads use secure URLs (S3 pre-signed uploads planned)

## 🎺 Database Design

* Normalized PostgreSQL schema
* Relationship mapped using JPA
* Includes key entities:

  * User, AnonUser, College
  * Canteen, MenuItem, Media
  * Cart, Order, Payment
  * SocialPost (confession, meme, etc.)

> For profile showcase, you **can** include a simplified ER diagram using draw\.io or dbdiagram.io. Just avoid including any seed/mock data that reveals real emails, payment keys, or user details.

## 🧪 Testing

* Manual testing with Postman
* Swagger for API validation
* Basic request validation in DTOs
* Future Scope: Add JUnit and integration tests with Testcontainers

## 🧰 Future Improvements

* 
* Add Razorpay support alongside PhonePe
* Integrate push notifications for tagged confessions
* Improve UI feed performance with lazy loading and pagination
* Admin analytics dashboard (orders, top users, etc.)

## Perfomance Improvement plan 
* Use Connection Pooling
* Reduce Logging Overhead (logging.level.root=WARN)
* Use Light Image Thumbnails (No full res img in list)
* Will move the media upload from the backedn by using presigned url (same as coreDx)
* Use Redis (inc TTL)
* Enable GZIP Compression in Nginx


## 📸 Screenshots / GIFs

> (Add demo GIFs or screenshots for canteen orders, confessions, dashboards, etc.)

## 📌 License & Author

* Built with ❤️ by [Ritik Kumar](https://github.com/Ritikkumar352)
* MIT License or private license depending on usage

---

**Note**: This project is being actively developed as part of a final year engineering project and aims to evolve into a full-scale college utility platform.
