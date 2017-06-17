namespace DataAccessLayer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class identitymigration4 : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.AspNetUsers", "UserProfile_Id", c => c.Int());
            CreateIndex("dbo.AspNetUsers", "UserProfile_Id");
            AddForeignKey("dbo.AspNetUsers", "UserProfile_Id", "dbo.UserProfile", "Id");
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.AspNetUsers", "UserProfile_Id", "dbo.UserProfile");
            DropIndex("dbo.AspNetUsers", new[] { "UserProfile_Id" });
            DropColumn("dbo.AspNetUsers", "UserProfile_Id");
        }
    }
}