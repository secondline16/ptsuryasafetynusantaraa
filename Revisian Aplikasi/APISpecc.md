### Login

Request :

- Method : POST
- Endpoint : `/auth/login`
- Header :
  - Content-Type: application/json
  - Accept: application/json
- Body :

```json
{
  "email": "string, unique",
  "password": "string",
  "firebase_token": "string"
}
```

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": {
    "id": "integer, unique",
    "username": "string, nullable"
    "name": "string, null",
    "email": "string, unique",
    "phone": "string, nullable",
    "address": "string, nullable",
    "photo": "string",
    "api_token": "string"
  }
}
```

### Register

Request :

- Method : POST
- Endpoint : `/auth/register`
- Header :
  - Content-Type: application/json
  - Accept: application/json
- Body :

```json
{
  "username": "string, unique",
  "password": "string",
  "name": "string, null",
  "email": "string, unique",
  "phone": "string, nullable",
  "address": "string, nullable"
}
```

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": null
}
```

### Pengaturan Akun (Get User Data)

Request :

- Method : GET
- Endpoint : `/profile/{id}`
- Header :
  - Content-Type: application/json
  - Accept: application/json
  
Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": {
    "id": "integer, unique",
    "username": "string, nullable",
    "name": "string, nullable",
    "email": "string, unique",
    "phone": "string, nullable",
    "address": "string, nullable",
    "photo": "string",
    "api_token": "string"
  }
}
```

### Edit Akun (PUT Edit Profile)

Request :

- Method : PUT
- Endpoint : `/profile/edit`
- Header :
  - Content-Type: application/json
  - Accept: application/json
- Body :

```json
{
  "username": "string, unique",
  "name": "string, null",
  "phone": "string, nullable",
  "address": "string, nullable"
}
```

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": {
    "id": "integer, unique",
    "username": "string, nullable",
    "name": "string, null",
    "email": "string, unique",
    "phone": "string, nullable",
    "address": "string, nullable",
    "photo": "string",
    "api_token": "string"
  }
}
```

### UPLOAD Avatar PHOTO (POST Edit Avatar)

Request :

- Method : PUT
- Endpoint : `/profile/avatar/edit`
- Header :
  - Content-Type: application/json
  - Accept: application/json
- Body

```json
{
  "photo": "file, jpg,png"
}
```

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": "string, url"
}
```

### List Lowongan Pekerjaan (GET List Vacancies)

Request :

- Method : GET
- Endpoint : `/vacancies`
- Header :
  - Content-Type: application/json
  - Accept: application/json

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": [
      {
        "id": "integer",
        "company_name": "string, nullable",
        "job_position": "string, nullable",
        "deadline": "string, [2020-07-09 23:18:01]"
      },
      {
        "id": "integer",
        "company_name": "string, nullable",
        "job_position": "string, nullable",
        "deadline": "string, [2020-07-09 23:18:01]"
      },
  ]
}
```

### Detail Lowongan Pekerjaan (Get Detail Vacancies)

Request :

- Method : GET
- Endpoint : `/vacancy/{id}`
- Header :
  - Content-Type: application/json
  - Accept: application/json

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": {
    "id": "integer",
    "company_name": "string, nullable",
    "job_position": "string, nullable",
    "deadline": "string, [2020-07-09 23:18:01]",
    "job_description": "string, nullable",
    "job_requirements": "string, nullable"
}
```

### List Daftar Pelatihan (GET List Trainings)

Request :

- Method : GET
- Endpoint : `/trainings`
- Header :
  - Content-Type: application/json
  - Accept: application/json

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": [
      {
        "id": "integer",
        "training_name": "string, nullable",
        "training_start": "string, nullable",
        "training_end": "string, nullable",
        "training_price": "integer, nullable"
      },
      {
        "id": "integer",
        "training_name": "string, nullable",
        "training_start": "string, nullable",
        "training_end": "string, nullable",
        "training_price": "integer, nullable"
      },
  ]
}
```

### Detail Pelatihan (GET Detail Trainings)

Request :

- Method : GET
- Endpoint : `/training/{id}`
- Header :
  - Content-Type: application/json
  - Accept: application/json

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": {
    "id": "integer",
    "training_img": "string, url",
    "training_name": "string, nullable",
    "training_start": "string, nullable [2020-07-09 23:18:01]",
    "training_end": "string, nullable [2020-07-09 23:18:01]",
    "training_price": "integer, nullable",
    "training_desc": "integer, nullable",
    "status": "boolean [true=Sudah Pernah daftar pelatihan, false=belum pernah daftar pelatihan]"
    }
}
```

### Form Daftar Pelatihan (POST Register Training)

Request :

- Method : POST
- Endpoint : `/training/register`
- Header :
  - Content-Type: application/json
  - Accept: application/json
- Body :

```json
{
  "training_id": "integer",
  "customer_id": "integer",
  "invoice_proof": "file"
}
```

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": null
}
```

### List Pelatihan Diikuti (GET List Trainings Records)

Request :

- Method : GET
- Endpoint : `/followup_trainings` [training_records]
- Header :
  - Content-Type: application/json
  - Accept: application/json

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": [
      {
        "id": "integer",
        "training_name": "string, nullable",
        "status": "string"
      },
      {
        "id": "integer",
        "training_name": "string, nullable",
        "status": "string"
      },
  ]
}
```

### Detail Pelatihan Diikuti (GET Detail Trainings Records)

Request :

- Method : GET
- Endpoint : `/followup_training/{training_record_id}`
- Header :
  - Content-Type: application/json
  - Accept: application/json

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data":
      {
        "id": "integer",
        "training_img": "string, nullable",
        "training_name": "string, nullable",
        "training_desc": "integer, nullable",
        "training_start": "string, nullable",
        "training_end": "string, nullable",
        "trainer_name": "string, nullable",
        "status": "string, [Selesai, Belum Selesai]",
        "trainer_cv": "string, url",
        "training_materials": "string, url",
        "requirement_status": "string, [Lengkap, Belum Lengkap]",
        "training_certificate": "string, url [pdf]",
        "competence_certificate": "string, url [pdf]"
	"whatsapp_group": "string, nullable"
      },
}
```

### UPLOAD PERSYARATAN (POST UPLOAD Training Requirements)

Request :

- Method : POST
- Endpoint : `/training/requirements [upload]`
- Header :
  - Content-Type: application/json
  - Accept: application/json
- Body

```json
{
  "cv": "file, pdf,jpg,png",
  "ktp": "file, pdf,jpg,png",
  "ijazah": "file, pdf,jpg,png",
  "work_experience": "file, pdf,jpg,png",
  "portofolio": "file, pdf,jpg,png",
  "optional_file": "file, pdf,jpg,png [optional]"
}
```

Response :

```json
{
  "meta": {
    "code": "integer",
    "message": "string"
  },
  "data": null
}
```
