# Haneef Fix this
- issue in UserResponseDto, fix  --> done
- Add this to User Entity -- to access all orders placed by a user
- ``` @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<Order> orders;

# Ritik fix this





# TODO
- 


## ~
- Request Part -> Json + file
- Request Body -> Json (e.g DTO)