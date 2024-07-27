package com.coolgirl.majko.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.navigation.Screen

@Composable
fun GroupCard(navHostController: NavHostController,
              priorityColor : Int = R.color.white,
              groupData: GroupResponse, ) {

    var tapType by remember { mutableStateOf(R.color.gray) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(start = 10.dp, top = 10.dp, end = 10.dp)
        .clip(RoundedCornerShape(20.dp))
        .border(3.dp, color = colorResource(tapType), shape = RoundedCornerShape(20.dp))
        .background(color = colorResource(priorityColor))
        .clickable { navHostController.navigate(Screen.GroupEditor.createRoute(groupData.id)) },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        Row(
            Modifier
                .padding(start = 15.dp, top = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.27f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically){
            Box(Modifier.
            fillMaxHeight(0.8f)
                .size(25.dp)
                .aspectRatio(1f)
                .background(MaterialTheme.colors.primary, shape = CircleShape))
           /* Image(painter = painterResource(R.drawable.icon_plug),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(CircleShape))*/
            Spacer(Modifier.width(15.dp))
            Text(text= groupData.title?: stringResource(R.string.common_noname), modifier = Modifier.fillMaxWidth(0.7f), fontSize = 14.sp, fontWeight = FontWeight.Medium, softWrap = true, maxLines = 2)

        }
        Row(
            Modifier
                .padding(start = 15.dp, top = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top){
            Text(text= groupData.description?: stringResource(R.string.common_nodescription), fontSize = 13.sp, fontWeight = FontWeight.Light, softWrap = true, maxLines = 9)
        }
        Row(
            Modifier
                .fillMaxSize()
                .padding(end = 15.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End) {
            if (!groupData.isPersonal){
                Image(painter = painterResource(R.drawable.icon_members),
                    contentDescription = "")
                Text(text = groupData.members.size.toString())
            }
        }
    }
}