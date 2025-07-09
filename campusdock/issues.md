# Issues

## 0. Review those 2 added DTOs and 4th method in cartServiceImpl

## 1. Menu Item, Cart, User

- Added menu Item to the user cart , but the is of that menu item gets changed in the user cart

- Added menu Item `d5633bce-762e-4d18-874e-d8a4f44585af` to the user Id `c6dbfbe8-7fe7-4ba7-9b66-5ecdc4c162f1`

```
{
    "userId":"c6dbfbe8-7fe7-4ba7-9b66-5ecdc4c162f1",
    "menuItemId":"d5633bce-762e-4d18-874e-d8a4f44585af",
    "quantity":"4"
}
```

- Cart of that same user  `c6dbfbe8-7fe7-4ba7-9b66-5ecdc4c162f1`

```
{
    "id": "1e690a4c-73e9-4370-bbbc-7a7149da4203",
    "items": [
        {
            "id": "5cd650a9-2686-4335-87ec-3004a16fe5e2",
            "status": "ADDED",
            "quantity": 1
        },
        {
            "id": "ca581308-437a-46b6-b011-2e94e185251f",
            "status": "ADDED",
            "quantity": 4
        }
            ]
}
```

## 2. Handle Errors and exceptions 

- 1. College Domain not registered 500
- 2. Item not found Error


## 3. Creating User Error
- Role is marked as Not null, due to this registration is getting failed