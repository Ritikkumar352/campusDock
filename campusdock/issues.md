# Issues

## 0. ~~test using .env~~
## 01.~~use dto to return list of all studetns, faculty and canteen lowing down whole student entity~~
## 02 Use Fetch = fetchType.Lazy in all mappings
## 03. Use a Set instead of a List for @OneToMany relationships


## 1. Cannot Add menu Item from different Canteen

- Clear Canteen Id when user remove all menu Items <-need to do this
- Clear Canteen Id when useers delete their cart  <- need to do this 


## 2. Handle Errors and exceptions 

- 1. College Domain not registered 500
- 2. Item not found Error


## 3. Faculty and Student list 
- long delay 5-7 sec

## fix get menu Item detail delay ->
- In return type use only url and img name (for link other than details) 

# Add new API's

1. PATCH /api/v1/menuItems/{menuItemId}/toggle-availability -- to a update menu item availability
2. POST /api/v1/menuItems/canteens/{canteenId} (add menu item)
  3. DELETE /api/v1/menuItems/{menuItemId} (delete menu item)
   4. PATCH /api/v1/menuItems/{menuItemId}/toggle-availability (toggle availability)
   5. GET /api/v1/menuItems/canteens/{canteenId} (fetch all menu items for a canteen)
   6. GET /api/v1/menuItems/{menuItemId} (fetch menu item details)


## From ReadMe.md (temp) -> Review and remove 

# Issues

## 1. Cannot Add menu Item from different Canteen

- Clear Canteen Id when user remove all menu Items <-need to do this
- Clear Canteen Id when useers delete their cart  <- need to do this


## 2. Handle Errors and exceptions

- 1. College Domain not registered 500
- 2. Item not found Error


## 3. Creating User Error
- Role is marked as Not null, due to this registration is getting failed

## 4. Handle Errors and exceptions 

- 1. College Domain not registered 500
- 2. Item not found Error


## 3. Creating User Error 
- Role is marked as Not null, due to this registration is getting failed


- trigger rebuild