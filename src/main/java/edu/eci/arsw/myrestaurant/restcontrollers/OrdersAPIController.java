/*
 * Copyright (C) 2016 Pivotal Software, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.arsw.myrestaurant.restcontrollers;

import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.ProductType;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.services.OrderServicesException;
import edu.eci.arsw.myrestaurant.services.RestaurantOrderServicesStub;
import edu.eci.arsw.myrestaurant.beans.impl.BasicBillCalculator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/orders")
public class OrdersAPIController {
     private RestaurantOrderServicesStub orderService = new RestaurantOrderServicesStub();
     @RequestMapping(method = RequestMethod.GET)
     public ResponseEntity<?> GetOrder() {
          try {
               Set<Integer> response = orderService.getTablesWithOrders();
               List<Object> listResponse = new ArrayList<>();
               for (Integer n: response) {
                    Order order = orderService.getTableOrder(n);
                    listResponse.add(order);
                    orderService.setBillCalculator(new BasicBillCalculator());
                    listResponse.add("Total de orden " + n + ": " + orderService.calculateTableBill(n));
               }
               return new ResponseEntity<>(listResponse,HttpStatus.OK);
          } catch (Exception ex) {
               return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);
          }
     }
}
