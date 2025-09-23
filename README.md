# Medical Billing Application

Eine Spring Boot REST API für Privatliquidation. Das System verwaltet Rechnungsempfänger (Debtors), Leistungserbringer (Provider), Rechnungen, Zahlungen und Mahnwesen.

## Projektübersicht

Diese Anwendung verwaltet die Privatliquidation für medizinische Leistungserbringer. Kernfunktionen sind die Erstellung von Rechnungen, Zahlungserfassung und automatisches Mahnwesen.

**Hauptentitäten:**

- **Debtor:** Rechnungsempfänger (Patienten)
- **Provider:** Leistungserbringer (Ärzte, Praxen, Kliniken)
- **ServiceCatalog:** Leistungskatalog mit GOÄ-Codes
- **Invoice:** Rechnungen mit Positionen
- **Payment:** Zahlungseingänge
- **DunningLog:** Mahnhistorie

## Tech Stack

- Java 17
- Spring Boot 3.5.6
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok

## Setup

### Voraussetzungen

- Java 17+
- Maven 3.6+
- PostgreSQL

### PostgreSQL Setup

Erstelle eine Datenbank:

```sql
CREATE DATABASE medical_billing;
```

### Anwendung starten

```bash
./mvnw spring-boot:run
```

Die Anwendung läuft auf: `http://localhost:8080`

## API Endpoints

### Rechnungsempfänger (Debtors)

#### Neuen Rechnungsempfänger erstellen

```
POST /api/v1/debtors
Content-Type: application/json

{
  "firstName": "Mouayad",
  "lastName": "Omar",
  "dateOfBirth": "1985-06-15",
  "email": "mouayad.omar@gmail.com",
  "address": "Berliner Straße 45, 10115 Berlin",
  "payerType": "PRIVATE_INSURANCE",
  "payerDetails": "DKV Krankenversicherung, Nr: 987654321"
}
```

#### Alle Rechnungsempfänger abrufen

```
GET /api/v1/debtors?pageNo=0&pageSize=10&sortBy=lastName&sortDir=asc
```

#### Rechnungsempfänger suchen

```
GET /api/v1/debtors?name=Omar&pageNo=0&pageSize=10
```

#### Einzelnen Rechnungsempfänger abrufen

```
GET /api/v1/debtors/{id}
```

### Leistungserbringer (Provider)

#### Neuen Provider erstellen

```
POST /api/v1/providers
Content-Type: application/json

{
  "name": "Dr. med. Adam Müller Hausarztpraxis",
  "type": "PRAXIS",
  "specialty": "Allgemeinmedizin",
  "taxId": "12/345/67890"
}
```

#### Alle Provider abrufen

```
GET /api/v1/providers?pageNo=0&pageSize=10&sortBy=name&sortDir=asc
```

#### Provider suchen

```
GET /api/v1/providers?name=Müller&pageNo=0&pageSize=10
```

#### Einzelnen Provider abrufen

```
GET /api/v1/providers/{id}
```

### Leistungskatalog (Services)

#### Neue Leistung erstellen

```
POST /api/v1/services
Content-Type: application/json

{
  "code": "1",
  "description": "Beratung",
  "baseFeeCents": 1051
}
```

#### Alle Leistungen abrufen

```
GET /api/v1/services?pageNo=0&pageSize=20&sortBy=code&sortDir=asc
```

#### Leistung suchen

```
GET /api/v1/services?code=1&pageNo=0&pageSize=10
```

#### Einzelne Leistung abrufen

```
GET /api/v1/services/{id}
```

### Rechnungen (Invoices)

#### Neue Rechnung erstellen

```
POST /api/v1/invoices
Content-Type: application/json

{
  "providerId": 1,
  "debtorId": 1,
  "invoiceDate": "2025-09-23",
  "dueInDays": 30,
  "items": [
    {
      "serviceId": 1,
      "quantity": 1,
      "factor": 2.3,
      "surchargeCents": 0
    }
  ]
}
```

#### Rechnungen abrufen (mit Filtern)

```
GET /api/v1/invoices?status=OPEN&from=2025-09-01&to=2025-09-30&pageNo=0&pageSize=20
```

#### Einzelne Rechnung abrufen

```
GET /api/v1/invoices/{id}
```

#### Rechnung ändern

```
PUT /api/v1/invoices/{id}
Content-Type: application/json

{
  "invoiceDate": "2025-09-23",
  "dueInDays": 45,
  "items": [
    {
      "serviceId": 1,
      "quantity": 1,
      "factor": 3.5,
      "surchargeCents": 100
    }
  ]
}
```

#### Rechnung stornieren

```
POST /api/v1/invoices/{id}/cancel
```

### Zahlungen (Payments)

#### Zahlung erfassen

```
POST /api/v1/invoices/{id}/payments
Content-Type: application/json
Idempotency-Key: unique-payment-id

{
  "amountCents": 2417,
  "paymentDate": "2025-09-23",
  "method": "BANK",
  "note": "Überweisung"
}
```

#### Zahlungshistorie abrufen

```
GET /api/v1/invoices/{id}/payments
```

### Mahnwesen (Dunning)

#### Mahnlauf durchführen

```
POST /api/v1/dunning/run?asOf=2025-09-23
```

#### Mahnhistorie abrufen

```
GET /api/v1/invoices/{id}/dunning-logs
```

### Anhänge (Attachments)

#### Datei hochladen

```
POST /api/v1/invoices/{id}/attachments
Content-Type: multipart/form-data

Form-Data: file=@rechnung.pdf
```

#### Dateien einer Rechnung abrufen

```
GET /api/v1/invoices/{id}/attachments
```

#### Datei herunterladen

```
GET /api/v1/invoices/{id}/attachments/{attachmentId}
```

#### Datei löschen

```
DELETE /api/v1/invoices/{id}/attachments/{attachmentId}
```

### Berichte (Reports)

#### Rechnungen als CSV exportieren

```
GET /reports/invoices.csv?status=PAID&date_from=2025-09-01&date_to=2025-09-30
```

### Import

#### CSV-Rechnungsimport

```
POST /import/invoices
Content-Type: multipart/form-data

Form-Data: file=@invoices.csv
```

## Enums

**PayerType:** SELF_PAY, PRIVATE_INSURANCE, BEIHILFE, OTHER
**ProviderType:** CHEFARZT, PRAXIS, KLINIK
**InvoiceStatus:** OPEN, PARTIALLY_PAID, PAID, CANCELLED
**PaymentMethod:** BANK, CASH, CARD
**DunningLevel:** NONE, LEVEL_1, LEVEL_2, COLLECTION

## Konfiguration

Die Anwendung nutzt PostgreSQL als Datenbank. Konfiguration in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medical_billing
spring.datasource.username=postgres
spring.datasource.password=123456789
```


## Technologie-Stack

- **Java 17** mit **Spring Boot 3.5.6**
- **Spring Data JPA** 
- **H2 Database** oder **PostgreSQL**
- **Maven** 
- **Lombok** 
- **ModelMapper** 
- **Bean Validation** 

## Schnellstart

### Voraussetzungen

- Java 17 oder höher
- Maven 3.6+

### Anwendung starten

```bash
# Repository klonen
git clone <repository-url>
cd backend

# Mit Maven ausführen
./mvnw spring-boot:run

Die Anwendung startet auf `http://localhost:8080`

## Datenbank-Setup

### H2 Console

- **URL:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:medical_billing`
- **Benutzername:** `sa`
- **Passwort:** _(leer lassen)_

### PostgreSQL

`application.properties` aktualisieren:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medical_billing
spring.datasource.username=ihr_benutzername
spring.datasource.password=ihr_passwort
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## API-Tests mit Postman

### Basis-URL

```
http://localhost:8080
```

### Beispiel-API-Aufrufe

**1. Anbieter erstellen:**

```
POST /api/v1/providers
Content-Type: application/json

{
  "name": "Dr. med. Adam Müller Hausarztpraxis",
  "type": "PRAXIS",
  "specialty": "Allgemeinmedizin",
  "taxId": "12/345/67890"
}
```

**2. Rechnungsempfänger erstellen:**

```
POST /api/v1/debtors
Content-Type: application/json

{
  "firstName": "Mouayad",
  "lastName": "Omar",
  "dateOfBirth": "1985-06-15",
  "email": "mouayad.omar@gmail.com",
  "payerType": "PRIVATE_INSURANCE"
}
```

**3. Leistung erstellen:**

```
POST /api/v1/services
Content-Type: application/json

{
  "code": "1",
  "description": "Beratung",
  "baseFeeCents": 1051
}
```

**4. Rechnung erstellen:**

```
POST /api/v1/invoices
Content-Type: application/json

{
  "providerId": 1,
  "debtorId": 1,
  "invoiceDate": "2025-09-23",
  "dueInDays": 30,
  "items": [{
    "serviceId": 1,
    "quantity": 1,
    "factor": 2.3,
    "surchargeCents": 0
  }]
}
```

**5. Zahlung hinzufügen:**

```
POST /api/v1/invoices/1/payments
Content-Type: application/json
Idempotency-Key: payment-001

{
  "amountCents": 2417,
  "paymentDate": "2025-09-23",
  "method": "BANK",
  "note": "Überweisung von Mouayad Omar"
}
```

### Wichtige Endpoints

- `GET /api/v1/debtors` - Rechnungsempfänger mit Suche und Pagination
- `GET /api/v1/providers` - Leistungserbringer mit Filtern
- `GET /api/v1/invoices?status=OPEN&from=2025-09-01` - Erweiterte Rechnungssuche
- `POST /api/v1/dunning/run` - Mahnlauf ausführen
- `GET /reports/invoices.csv` - Rechnungen als CSV exportieren

## Datenmodell

**Kern-Entitäten:**

- **Provider:** Medizinische Leistungserbringer (Ärzte, Kliniken)
- **Debtor:** Patienten, die Rechnungen erhalten
- **ServiceCatalog:** Medizinische Leistungen mit GOÄ-Codes und Preisen
- **Invoice:** Hauptrechnungs-Entität mit Statusverfolgung
- **InvoiceItem:** Einzelne Leistungen einer Rechnung
- **Payment:** Zahlungsaufzeichnungen mit Methoden und Daten
- **DunningLog:** Automatisierter Mahnprozess-Verlauf

**Enums:**

- **ProviderType:** CHEFARZT, PRAXIS, KLINIK
- **PayerType:** SELF_PAY, PRIVATE_INSURANCE, BEIHILFE, OTHER
- **InvoiceStatus:** OPEN, PARTIALLY_PAID, PAID, CANCELLED
- **PaymentMethod:** BANK, CASH, CARD
- **DunningLevel:** NONE, LEVEL_1, LEVEL_2, COLLECTION

## Geschäftslogik

**Rechnungsworkflow:**

1. Rechnung mit Positionen erstellen
2. System berechnet Gesamtsumme automatisch
3. Zahlungen erfassen, wenn sie eingehen
4. Status wird automatisch aktualisiert (OPEN → PARTIALLY_PAID → PAID)
5. Automatischer Mahnprozess eskaliert überfällige Rechnungen

**Mahnverfahren:**

- 7 Tage nach Fälligkeit: LEVEL_1
- 21 Tage nach Fälligkeit: LEVEL_2
- 42 Tage nach Fälligkeit: COLLECTION

**Made With Love ^_^**