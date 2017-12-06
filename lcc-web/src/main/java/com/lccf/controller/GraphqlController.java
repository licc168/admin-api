package com.lccf.controller;

import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLObjectType.newObject;

import com.lccf.base.controller.BaseController;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lccf.service.user.IUserService;

import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLSchema;

/**
 *
 *  Graphql 列子
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/10/31 11:21
 * @see
 */
@RestController
public class GraphqlController extends BaseController {

  private static GraphQLOutputType userType;
  public static GraphQLSchema schema;
  @Resource
  IUserService userService;



  @GetMapping(value = "demo")
  public String demo(@RequestParam(value = "query", required = false) String query)
      throws JsonProcessingException {
    GraphSchema();
		/*
		 * Query example:
		 *
		 * String query1 = "{users(page:2,size:5,name:\"john\") {id,name}}";
		 * String query2 = "{user(id:6) {id,name}}";
		 * String query3 = "{user(id:6) {id,name},users(page:2,size:5,name:\"john\") {id,name}}"
		 * ;
		 */
    query = "{user(userName:\"licc168\") {id,userName}}";
    //Fetch the result with query string
    Map<String, Object> result = new GraphQL(schema).execute(query).getData();

    return new ObjectMapper().writeValueAsString(result == null ? "Invaid query! \n Please correct the query and try again. : )" : result);
  }


  /**
   * Init the output type
   */
  private void initOutputType() {

    userType = newObject().name("User")
        .field(GraphQLFieldDefinition.newFieldDefinition().name("id").type(Scalars.GraphQLLong).build())
        .field(GraphQLFieldDefinition.newFieldDefinition().name("userName").type(Scalars.GraphQLString).build())
        //.field(GraphQLFieldDefinition.newFieldDefinition().name("realName").type(Scalars.GraphQLString).build())
        .build();
  }

  /**
   * Check single user
   *
   */
  private GraphQLFieldDefinition createUserField() {
    return GraphQLFieldDefinition.newFieldDefinition().name("user")
        .argument(newArgument().name("userName").type(Scalars.GraphQLString).build()).type(userType).dataFetcher(environment -> {
          // 获取查询参数
          String userName = environment.getArgument("userName");


          // 执行查询, 这里随便用一些测试数据来说明问题
          return userService.getByUserName(userName);
        }).build();
  }

//  /**
//   * Check multiple users
//   *
//   */
//  private GraphQLFieldDefinition createUsersField() {
//    return GraphQLFieldDefinition.newFieldDefinition().name("users")
//        .argument(newArgument().name("page").type(Scalars.GraphQLInt).build())
//        .argument(newArgument().name("size").type(Scalars.GraphQLInt).build())
//        .argument(newArgument().name("name").type(Scalars.GraphQLString).build()).type(new GraphQLList(userType))
//        .dataFetcher(environment -> {
//          // 获取查询参数
//          int page = environment.getArgument("page");
//          int size = environment.getArgument("size");
//          String name = environment.getArgument("name");
//
//          // 执行查询, 这里随便用一些测试数据来说明问题
//          List<User> list = new ArrayList<>(size);
//          for (int i = 0; i < size; i++) {
//            User user = new User();
//            user.setId(i);
//            user.setAge((short)i );
//            user.setName("Name_" + i);
//            list.add(user);
//          }
//          return list;
//        }).build();
//  }


  private void GraphSchema() {
    initOutputType();
    schema = GraphQLSchema.newSchema().query(newObject()
        .name("GraphQuery")
        .field(createUserField())
        .build()).build();

  }

}
