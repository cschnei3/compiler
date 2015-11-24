//read numbers until 0 is read, and print their average

int main () 
{
  int sum = 0 ;
  int num = 0 ;
  int x ;
  printInt(77);
  x = readInt();
  printInt(x);
  while (x != 0) {
    sum = sum + x ;
    num++ ;
    printInt(701);
    x = readInt();
  }
  printInt(sum/num) ;

}
