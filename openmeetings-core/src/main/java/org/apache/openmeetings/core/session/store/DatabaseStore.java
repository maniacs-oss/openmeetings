/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openmeetings.core.session.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.openmeetings.db.dao.room.ClientDao;
import org.apache.openmeetings.db.entity.room.StreamClient;
import org.apache.openmeetings.db.entity.server.Server;
import org.springframework.beans.factory.annotation.Autowired;

public class DatabaseStore implements IClientPersistenceStore {

	@Autowired
	private ClientDao clientDao;

	@Override
	public void clear() {
		clientDao.cleanAllClients();
	}

	@Override
	public void put(String streamId, StreamClient rcl) {
		if (rcl.getId() != null) {
			clientDao.update(rcl);
		} else {
			clientDao.add(rcl);
		}
	}

	@Override
	public boolean containsKey(Server server, String streamId) {
		return clientDao.countClientsByServerAndStreamId(server, streamId) > 0;
	}

	@Override
	public StreamClient get(Server server, String streamId) {
		return clientDao.getClientByServerAndStreamId(server, streamId);
	}

	@Override
	public List<StreamClient> getClientsByPublicSID(Server server, String publicSID) {
		return clientDao.getClientsByPublicSIDAndServer(server, publicSID);
	}

	@Override
	public Map<Long, List<StreamClient>> getClientsByPublicSID(String publicSID) {
		Map<Long, List<StreamClient>> returnMap = new HashMap<>();
		List<StreamClient> clientList = clientDao.getClientsByPublicSID(publicSID);
		for (StreamClient cl : clientList) {
			if (cl.getServer() == null) {
				List<StreamClient> clList = returnMap.get(null);
				if (clList == null) {
					clList = new ArrayList<>();
				}
				clList.add(cl);
				returnMap.put(null, clList);
			} else {
				List<StreamClient> clList = returnMap.get(cl.getServer().getId());
				if (clList == null) {
					clList = new ArrayList<>();
				}
				clList.add(cl);
				returnMap.put(cl.getServer().getId(), clList);
			}
		}
		return returnMap;
	}

	@Override
	public Collection<StreamClient> getClients() {
		return clientDao.getClients();
	}

	@Override
	public Collection<StreamClient> getClientsWithServer() {
		return clientDao.getClientsWithServer();
	}

	@Override
	public Collection<StreamClient> getClientsByServer(Server server) {
		return clientDao.getClientsByServer(server);
	}

	@Override
	public List<StreamClient> getClientsByUserId(Server server, Long userId) {
		return clientDao.getClientsByUserId(server, userId);
	}

	@Override
	public List<StreamClient> getClientsByRoomId(Long roomId) {
		return clientDao.getClientsByRoomId(roomId);
	}

	@Override
	public void remove(Server server, String streamId) {
		clientDao.removeClientByServerAndStreamId(server, streamId);
	}

	@Override
	public int size() {
		return clientDao.countClients();
	}

	@Override
	public int sizeByServer(Server server) {
		return clientDao.countClientsByServer(server);
	}

	@Override
	public Collection<StreamClient> values() {
		return clientDao.getClients();
	}

	@Override
	public String getDebugInformation(List<DEBUG_DETAILS> detailLevel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> getRoomsIdsByServer(Server server) {
		return clientDao.getRoomsIdsByServer(server);
	}

}
