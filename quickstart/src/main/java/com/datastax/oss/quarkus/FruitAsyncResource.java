/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.oss.quarkus;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/async-fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitAsyncResource {
  private static final String STORE_NAME = "acme";
  @Inject FruitAsyncService service;

  @GET
  public Multi<Fruit> getAll() {
    return service.get(STORE_NAME);
  }

  @POST
  public Multi<Fruit> add(FruitDto fruitDto) {
    Fruit fruit = covertFromDto(fruitDto);
    return Uni.createFrom().completionStage(service.add(fruit)).then(ignored -> getAll());
  }

  private Fruit covertFromDto(FruitDto fruitDto) {
    return new Fruit(fruitDto.getName(), fruitDto.getDescription(), STORE_NAME);
  }
}