package com.example.decideai_front.ui.features

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.decideai_front.viewmodel.FriendsViewModel
import com.example.decideai_front.viewmodel.GroupDecisionViewModel

@Composable
fun InviteFriendsScreen(
    friendsVm: FriendsViewModel,
    groupVm: GroupDecisionViewModel,
    token: String,
    onSuccess: () -> Unit
) {
    LaunchedEffect(Unit) {
        friendsVm.loadFriends(token)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Convide seus amigos", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = friendsVm.searchQuery,
            onValueChange = { friendsVm.searchUsers(token, it) },
            label = { Text("Digite o nome...") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(friendsVm.friendsList) { friend ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = friend.avatar,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(friend.username, modifier = Modifier.weight(1f))
                    Checkbox(
                        checked = groupVm.selectedFriendsIds.contains(friend.id),
                        onCheckedChange = { isChecked ->
                            if (isChecked) groupVm.selectedFriendsIds.add(friend.id)
                            else groupVm.selectedFriendsIds.remove(friend.id)
                        }
                    )
                }
            }
        }

        Button(
            onClick = { groupVm.createDecision(token, onSuccess) },
            modifier = Modifier.fillMaxWidth(),
            enabled = groupVm.selectedFriendsIds.isNotEmpty()
        ) {
            Text("Iniciar Decisão")
        }
    }
}