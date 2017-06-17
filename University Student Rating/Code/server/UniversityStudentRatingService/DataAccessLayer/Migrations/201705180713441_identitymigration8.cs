namespace DataAccessLayer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class identitymigration8 : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.AspNetUsers", "UserProfile_Id", "dbo.UserProfile");
            DropIndex("dbo.AspNetUsers", new[] { "UserProfile_Id" });
            DropColumn("dbo.AspNetUsers", "UserProfile_Id");
        }
        
        public override void Down()
        {
            AddColumn("dbo.AspNetUsers", "UserProfile_Id", c => c.Int());
            CreateIndex("dbo.AspNetUsers", "UserProfile_Id");
            AddForeignKey("dbo.AspNetUsers", "UserProfile_Id", "dbo.UserProfile", "Id");
        }
    }
}
